# Twilight Forest → Fabric Port Journal

**Цель:** порт с NeoForge 21.4.124 / MC 1.21.4 → чистый Fabric 0.141.4 / MC 1.21.11  
**Ветка:** `fabric-port`  
**Стратегия:** «compile-first» — сначала добиться компиляции, потом рантайм

---

## ⚠️ ВАЖНО: это ДВОЙНОЙ порт

Это не только NeoForge→Fabric, но и **скачок MC 1.21.4 → 1.21.11** (7 версий).
Большинство ошибок — от смены MC, а не загрузчика:
- **1.21.5**: переработка NBT — `CompoundTag` геттеры теперь `Optional<>`, сохранение
  через `ValueInput`/`ValueOutput` вместо `CompoundTag`.
- **1.21.6**: полная переработка рендер-пайплайна — класс `RenderType` убран,
  заменён на пакет `client/renderer/rendertype/` (`RenderSetup`, `RenderPipeline`). ~330 каскадов.
- Маппинги 1.21.11 (`layered+hash.2198`) переименовали много классов/методов:
  - `net.minecraft.resources.ResourceLocation` → `Identifier` (это **Mojang**-маппинг, не Yarn!)
  - `ResourceKey.location()` → `identifier()`
  - `Level.noCollission()` → `noCollision()` (исправлена опечатка)
  - `Entity.moveTo(x,y,z[,yaw,pitch])` → `snapTo(...)` (но навигационный `moveTo(...,speed)` НЕ тронут)

## Текущее состояние компиляции

**~2234 уникальных ошибок** (на старте сессии было ~5036, −56%). cap javac снят через `-Xmaxerrs 20000` в build.gradle (временно, убрать в конце).

### Свежие сделанные пачки (после ~2497)
- IPayloadContext → compat-шим (redirect 23 импортов + `flow()`); PieceBeardifierModifier compat-интерфейс (22 структуры)
- LazyLoadedValue→`Suppliers.memoize`; BannerPatternItem + inline tool-конструкторы → `Item`/`Properties.<tool>`
- Wraith `extends FlyingMob`→`Mob` (FlyingMob удалён, слит в Mob — каскад −71)
- **ValueInput/ValueOutput (сущности)**: `add/readAdditionalSaveData(CompoundTag)`→`(ValueOutput/ValueInput)` в 33 файлах;
  helper-методы (`EnforcedHomePoint`, `SlideBlock`) на codec `store/read`. **Остался BE-side (9 файлов с `HolderLookup.Provider`).**
- `holderOrThrow`→`getOrThrow`, `WeightedList.create`→`of`, `getSlotForHand` unwrap, `toStack()` в DeferredHolder,
  откат over-reach `.location()`→`.identifier()` на SoundEvent/TagKey.

### Главные оставшиеся блоки (по убыванию объёма)
1. **Рендер/модели** (~400+): `BakedModel`/`IDynamicBakedModel`/`ModelData`/`ModelProperty`/`ChunkRenderTypeSet`/
   `RenderTypeGroup`/`entityCutoutNoCull`/`RenderTypeHelper`/`DelegateBakedModel`/`PlayerRenderState`/RenderSystem GL-стейт
   (`enableBlend`/`disableBlend`/`enableDepthTest`). Кастомные модели TF (Connected/ForceField/GiantBlock/Patch/NoiseVarying).
2. **BE ValueInput/Output** (9 файлов): убрать `HolderLookup.Provider`-параметр, codec-вызовы через `output.store`/`input.read`.
3. **События** (`BlockEvent`/`PlayerInteractEvent`/`PlayerEvent`/`LivingIncomingDamageEvent`/`PlayerTickEvent`...):
   ~25 хендлеров (EntityEvents/ToolEvents/CharmEvents/CapabilityEvents/...) → на Fabric callbacks (или стабы классов событий).
4. **Networking регистрация** (`RegisterPayloadHandlersEvent`/`PayloadRegistrar`) → Fabric `PayloadTypeRegistry` + `ServerPlayNetworking`.
5. Точечное: command permissions (`hasPermission(int)`→PermissionSet), `FlowerPotBlock.addPlant`, `getPersistentData`, data maps (`TFDataMaps`).

Доп. сделано после multipart: appendHoverText (5-арг, TooltipDisplay+Consumer, .add→.accept, 30 файлов),
tool-items off SwordItem/PickaxeItem/AxeItem (Properties.pickaxe/sword/axe; hurtEnemy теперь void),
`DeferredHolder.toStack()` через ItemLike, `holderOrThrow`→`getOrThrow`, registry `.get()`→`.getValue()` в шиме,
`level.getOrThrow(enchant)`→`registryAccess().lookupOrThrow(ENCHANTMENT).getOrThrow(...)`.

### Как продолжить (HANDOFF — читать первым)
1. Замер: `./gradlew compileJava --no-daemon > /tmp/full.txt 2>&1; grep -E '^D:.*error:' /tmp/full.txt | sort -u | wc -l`
2. Рейтинг типов: `grep -oE "error: [a-z][^;]*" /tmp/full.txt | sed -E 's/[0-9]+//g; s/'\''[^'\'']*'\''//g' | sort | uniq -c | sort -rn | head`
3. Топ отсутствующих классов (есть в jar = переехал пакет): см. приём ниже.
4. **probe.jar** = mapped MC jar для javap-проверки реальных сигнатур/имён:
   `cp .gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-merged-*/1.21.11-*/*.jar ~/probe.jar`
   затем `javap -p -s -classpath ~/probe.jar net.minecraft.<class>`.
5. Python = `/c/Python314/python.exe` (НЕ `python3`; читает только Windows-пути, пиши temp-файлы в репо).
6. После каждого коммита: `git push origin fabric-port`.

### Переиспользуемые приёмы (скрипты были одноразовыми, паттерн важен)
- **Авто-ремап импортов пакетов**: построить индекс `simpleName→FQN` из probe.jar
  (`unzip -l ~/probe.jar | grep -oE 'net/minecraft/[A-Za-z0-9/]+\.class' | grep -v '$'`),
  для каждого битого `import net.minecraft...` с ЕДИНСТВЕННЫМ кандидатом — переписать. (27 ремапов за раз.)
- **Авто-AccessWidener**: для «X has private access in Y» — найти FQN Y и дескриптор поля/метода
  через `javap -p -s`, дописать `accessible field/method ...` в twilightforest.accesswidener.
- **Error-driven правки NBT**: парсить `_port_*.txt` из ошибок, точечно менять только помеченные строки
  (paren-balanced). Так безопасно: не трогаем не-NBT вызовы с тем же именем.

### Что сделано в этой сессии (сверх раздела ниже)
- Particle rework: compat-база `twilightforest.client.particle.TextureSheetParticle` на `SingleQuadParticle`
  (тот же пакет → 13 партиклов компилируются; `createParticle` +RandomSource; `ParticleRenderType.*`→`SINGLE_QUADS`). −~460
- Multipart: `TFPart extends EntityPart` (lib nordmods), боссы `implements MultipartEntity`,
  `getParts()→EntityPart[]`, вызовы `isMultipartEntity()`→`instanceof MultipartEntity`. −~301
- NBT 1.21.5: `getInt(k)`→`getIntOr(k,0)` и т.д., `contains(k,t)`→`contains(k)`, `getList(k,t)`→`getListOrEmpty(k)`,
  `getCompound` у parse()→`getCompoundOrEmpty`. `Codec.unit`→`MapCodec.unitCodec` (DFU 9.0.19).
- BlockEntityType: приватный ctor+Set<Block> → `FabricBlockEntityTypeBuilder.create(f, blocks).build()`.
- SpawnerData: вес ушёл из record в `addSpawn(cat, weight, data)` (35 инлайн-вызовов).
- jsr305 (javax.annotation), стабы `@SubscribeEvent/@EventBusSubscriber/@OnlyIn/EventPriority`, убрана beanification.
- AW: WoodType/BlockSetType.register, + 27 полей.

### Осталось (крупные «настоящие» рефакторинги, библиотеки НЕ закроют — это ванильные изменения)
- **Рендер/модели**: `BakedModel`/`IDynamicBakedModel`/`ModelData`/`ModelProperty`/`ChunkRenderTypeSet`/
  `RenderTypeGroup`/`entityCutoutNoCull` — кастомные модели (NeoForge client model API). Porting Lib нет под 1.21.11.
- **ValueInput/ValueOutput** (~100): `read/addAdditionalSaveData(CompoundTag)` → новые сигнатуры BE/Entity.
- **Networking** (`IPayloadContext`, `network.handling`, ~46): портировать на Fabric Networking API.
- **События** (`BlockEvent`/`PlayerInteractEvent`/`LivingIncomingDamageEvent`...): на Fabric callbacks.
- **SpawnerData в ControlledSpawningConfig** (~142): нужен weight-несущий record (TF-data-model рефактор).
- `appendHoverText` новая сигнатура (TooltipDisplay+Consumer), `FlowerPotBlock`/`addPlant`, partamerт частиц в рендере.

Команда замера:
```bash
./gradlew compileJava --no-daemon 2>&1 | grep -E '^D:.*error:' | sort -u | wc -l
```

### Крупнейшие категории (по убыванию)
| Категория | ~Ошибок | Статус |
|-----------|--------|--------|
| `RenderType` рендер-рework (1.21.6) | ~330 | ⏳ тяжёлое, чистый Fabric Renderer API |
| `PartEntity`/multipart (Hydra/Naga/UrGhast) | ~600 каскадов | 🔧 библиотека выбрана (см. ниже) |
| NeoForge event bus (`SubscribeEvent` и пр.) | ~много | ⏳ Фаза 2 |
| NBT/`ValueOutput`/`Optional` геттеры (1.21.5) | ~400 | ⏳ механически, но объёмно |
| NeoForge networking (`IPayloadContext`) | ~46 | ⏳ Фаза 3 |
| NeoForge client models (`BakedModel`,`ModelData`) | ~114 | ⏳ Фаза 7 |
| `AttachmentType` (NeoForge data attachments) | ~50 | ⏳ Fabric Attachment API или шим |
| JEI (`mezz.jei.*`) | ~150 | ⏳ заглушить/исключить |
| beanification `@Autowired` | ~24 | ⏳ удалить |

## Подключённые библиотеки (решение пользователя)
- ✅ **Forge Config API Port** (Fuzs) `21.11.1` — даёт NeoForge `ModConfigSpec` под
  родными пакетами. Сняло ВСЕ ~102 ошибки конфигов без правок кода.
  Maven: `https://raw.githubusercontent.com/Fuzss/modresources/main/maven/`
- 🔧 **Primitive-Multipart-Entities** (NordAct) `1.2.0-1.21.11` — multipart-сущности.
  Лежит в `libs/` (JitPack не умеет Loom-артефакты). API: интерфейс `MultipartEntity`
  с `getParts()`, класс `EntityPart extends Entity` (`owner`, `setRelativePos`).
  Требует адаптации иерархии `TFPart` → `EntityPart`, боссы → `implements MultipartEntity`.
- ❌ **Porting Lib** — НЕ существует под 1.21.11 (только до 1.21.1). Модели/networking/
  capabilities придётся делать на чистом Fabric API.

> `loader_version` поднят до `0.18.1` (требование Config API Port + MC 1.21.11).
> `build.gradle` временно содержит `-Xmaxerrs 20000` для полного замера ошибок.

---

## Прогресс сессии 2026-06-22 (диагностика + библиотеки)
- Удалены все 106 `package-info.java` (только nullability-аннотации, `javax.annotation`
  нет на classpath Fabric) → −1252 строки ошибок.
- **Registry shim доделан**: `DeferredHolder` теперь `implements Holder<R>` (делегирует на
  `Holder.Reference`, получаемый через `Registry.registerForHolder` в `initialize()`).
  Сняло ошибки `DeferredBlock cannot be converted to Holder<Block>` и `is(DeferredBlock)`.
- **`DeferredComponent<T> implements DataComponentType<T>`** + `DeferredRegister.DataComponents`/
  `registerComponent()`. `TFDataComponents` переведён на `DeferredComponent` → `stack.get(HOLDER)`
  снова компилируется (как в NeoForge). −90 ошибок `DataComponentHolder.get/getOrDefault`.
- Глобальные механические переименования: `.location()`→`.identifier()` (93),
  `noCollission`→`noCollision` (158).
- Подключены Forge Config API Port (−102) и Primitive-Multipart-Entities (в `libs/`).
- Итог: **~5036 → ~4800** уникальных ошибок.

### Прогресс сессии 2026-06-22 (часть 2 — библиотеки + массовые переносы)
- **Forge Config API Port** подключён → −102 (ModConfigSpec, без правок).
- **TFDataAttachments** → Fabric Data Attachment API (eager `AttachmentRegistry.create`);
  call-sites `getData`→`getAttachedOrCreate`, `setData`→`setAttached`, `hasData`→`hasAttached`,
  `removeData`→`removeAttached` (только для `TFDataAttachments.*`; `TFDataMaps.*` — отдельно!).
- `Codec.unit` удалён в datafixers 9.0.19 → `MapCodec.unitCodec` (6 мест).
- **JEI исключён** из компиляции (`sourceSets.main.java.exclude 'twilightforest/compat/jei/**'`) — нет JEI под 1.21.11.
- Стабы NeoForge-аннотаций: `@SubscribeEvent`, `@EventBusSubscriber`, `EventPriority` → −~130.
- **Массовый перенос пакетов 1.21.11** (скрипт: индекс классов из mapped jar →
  для каждого `import net.minecraft...` с единственным новым путём переписать):
  - `RenderType` → `client.renderer.rendertype.RenderType` (62 файла!)
  - `Boat/ChestBoat/AbstractBoat/Raft` → `vehicle.boat.*`
  - `AbstractArrow/Arrow/SpectralArrow/ThrownTrident` → `projectile.arrow.*`
  - `PaintingVariant/Painting` → `decoration.painting.*`
  - `Spider/Zombie`→`monster.spider/zombie`, `Sheep/Wolf/Cat/Ocelot`→`animal.*`,
    `AbstractHorse`→`animal.equine`, модели Skull/Slime/Skeleton/Pig/Chest→`model.*` подпакеты,
    `FogRenderer`→`renderer.fog`, `GameRules`→`level.gamerules`, `MobSpawnSettings`→`level.biome` и др. (27 remap).
- `ArmorItem` import добавлен в TFItems; `DeferredComponent` (data components) готов.
- **Итог сессии: ~5036 → ~3915** уникальных ошибок (всё закоммичено).

> ⚠️ Скрипт авто-ремапа импортов (Python) — переиспользуемый приём: построить индекс
> `ClassName→FQN` из mapped jar (`~/probe.jar`), переписать импорты с единственным кандидатом.

### Следующие высокоприоритетные шаги
1. `AttachmentType` (NeoForge) → Fabric Attachment API (`fabric-data-attachment-api-v1`) или шим.
2. Multipart-рефактор: `TFPart extends EntityPart`, боссы `implements MultipartEntity`,
   `entity.getParts()` → `((MultipartEntity) entity).getParts()`.
3. `moveTo(...,yaw,pitch)` → `snapTo(...)` точечно (не трогать навигационный `moveTo(...,speed)`).
4. JEI: исключить пакет `twilightforest/compat/jei` из компиляции или заглушить `mezz.jei.*`.
5. NBT 1.21.5: `CompoundTag` Optional-геттеры + `ValueInput`/`ValueOutput`.

## Что уже сделано

### Фаза 0 — Система сборки ✅
- `settings.gradle` — убран tf-asm subproject, добавлен FabricMC maven
- `gradle.properties` — MC 1.21.11, Fabric Loader 0.16.10, Fabric API 0.141.4+1.21.11
- `build.gradle` — полный переход с NeoForge ModDevGradle на Fabric Loom 1.17.12
- `gradle/wrapper/gradle-wrapper.properties` — Gradle 9.6.0 (требуется Loom 1.17.12)
- `src/main/resources/fabric.mod.json` — новый файл с entrypoints
- `src/main/resources/twilightforest.accesswidener` — конвертирован из AT формата в AW v2
- `src/main/resources/twilightforest.mixins.json` — новый файл

### Фаза 7 — tf-asm → Mixin классы ✅ (скелеты)
Все 14 ASM-трансформеров переписаны как Mixin-скелеты:

| Mixin | Файл | Inject point |
|-------|------|-------------|
| `BeardifierMixin` | `mixin/BeardifierMixin.java` | `compute` RETURN |
| `NoiseChunkGeneratorMixin` | `mixin/NoiseChunkGeneratorMixin.java` | `Beardifier.forStructuresInChunk` |
| `ChunkStatusTasksMixin` | `mixin/ChunkStatusTasksMixin.java` | `buildSurface` |
| `LevelMixin` | `mixin/LevelMixin.java` | `isRainingAt` RETURN |
| `StructureStartMixin` | `mixin/StructureStartMixin.java` | `loadStaticStart` RETURN |
| `ChunkGeneratorMixin` | `mixin/ChunkGeneratorMixin.java` | `findNearestMapStructure` RETURN |
| `LeashFenceKnotEntityMixin` | `mixin/LeashFenceKnotEntityMixin.java` | `survives` RETURN |
| `LivingEntityMixin` | `mixin/LivingEntityMixin.java` | `getVisibilityPercent` RETURN |
| `PathfinderMobMixin` | `mixin/PathfinderMobMixin.java` | `shouldStayCloseToLeashHolder` RETURN |
| `MushroomBlockMixin` | `mixin/MushroomBlockMixin.java` | `canSurvive` RETURN |
| `ServerEntityMixin` | `mixin/ServerEntityMixin.java` | `sendDirtyEntityData` REDIRECT |
| `BiomeColorsMixin` | `mixin/client/BiomeColorsMixin.java` | `lambda$static$0` RETURN |
| `EntityRenderDispatcherMixin` | `mixin/client/EntityRenderDispatcherMixin.java` | `getRenderer` RETURN |
| `HumanoidArmorLayerMixin` | `mixin/client/HumanoidArmorLayerMixin.java` | `renderArmorPiece` HEAD |
| `LevelRendererMixin` | `mixin/client/LevelRendererMixin.java` | `entitiesForRendering` REDIRECT |

Созданы:
- `mixin/util/TFBeardifierAccess.java` — Mixin interface injection
- `ASMHooks.java` — переписан без beanification, без TriState, чистый Java

### Entrypoints ✅
- `client/TwilightForestClient.java` — stub ClientModInitializer
- `datagen/TFDatagenEntrypoint.java` — stub DataGeneratorEntrypoint

### Фаза 1 — Registry shim ✅ (частично)
Создан Fabric-backed DeferredRegister shim:
- `compat/registry/DeferredRegister.java` — с nested `Blocks` и `Items` классами
- `compat/registry/DeferredHolder.java` — хранит зарегистрированное значение
- `compat/registry/DeferredBlock.java` — добавляет `.asItem()`, `.defaultBlockState()`
- `compat/registry/DeferredItem.java` — добавляет `.toStack()`

Произведена глобальная замена импортов `net.neoforged.neoforge.registries.*` → `twilightforest.compat.registry.*` во всех 1561 файлах.

### MC 1.21.11 API renames (глобально через sed) ✅
| Было | Стало |
|------|-------|
| `net.minecraft.resources.ResourceLocation` | `net.minecraft.resources.Identifier` |
| `net.minecraft.Util` | `net.minecraft.util.Util` |
| `import net.minecraft.world.item.ArmorItem` | `import twilightforest.compat.ArmorItem` |
| `SimpleWeightedRandomList<T>` | `WeightedList<T>` |
| `WeightedRandomList<T>` | `WeightedList<T>` |
| `WeightedEntry.Wrapper<T>` | `Weighted<T>` |
| `net.minecraft.world.entity.projectile.LargeFireball` | `...hurtingprojectile.LargeFireball` |
| `BlockEntity.DataComponentInput` | `DataComponentGetter` |
| `new Unbreakable(true/false)` | `Unit.INSTANCE` |
| `import net.minecraft.world.item.component.Unbreakable` | `import net.minecraft.util.Unit` |

### Compat classes ✅
- `compat/ArmorItem.java` — drop-in для `net.minecraft.world.item.ArmorItem` (удалён в 1.21.11)
  - принимает `(ArmorMaterial, ArmorType, Item.Properties)` с обоими вариантами
  - автоматически добавляет `EQUIPPABLE` компонент через `Equippable.builder()`

---

## Следующие шаги (в порядке приоритета)

### 1. Убрать beanification (`@Autowired`)
**Файлы:** `TFItems.java`, `TwilightForestMod.java`, и другие с `import tamaized.beanification.Autowired`  
**Действие:** удалить `import tamaized.beanification.Autowired`, убрать аннотации `@Autowired`,  
внедрять зависимости через обычные статические поля, инициализируемые в `onInitialize()`.

### 2. Добить MC 1.21.11 API changes
**Оставшиеся:**
- `SimpleCriterionTrigger` → проверить новое имя в MC 1.21.11 jar
- `ContextAwarePredicate` → проверить
- `SimpleInstance` (у criterion триггеров) → проверить
- `Component` (chat) — скорее всего просто пропавший import

### 3. TFPartEntity (Фаза 8) — критично
**Файлы:** `entity/TFPart.java`, `entity/boss/Hydra.java`, `entity/boss/Naga.java` и др.  
`PartEntity<T>` → создать `twilightforest.entity.TFPartEntity<T extends LivingEntity> extends Entity`  
без зависимости на NeoForge.

### 4. Убрать NeoForge события (Фаза 2) — 22 файла
Таблица замен в main plan ниже.

### 5. Убрать NeoForge networking (Фаза 3) — 14 файлов

### 6. Убрать NeoForge capabilities (Фаза 5)
`ItemStackHandler` → Fabric Transfer API / vanilla inventory

### 7. TwilightForestMod.java — зарегистрировать все DeferredRegister через `.initialize()`

---

## Ключевые архитектурные решения

1. **DeferredRegister shim** вместо переписки 2208+ `.get()` вызовов
2. **Mixin скелеты** вместо tf-asm submodule (14 классов)
3. **ArmorItem compat** вместо переписки всей системы брони
4. **Нет Architectury** — чистый Fabric
5. **Mojang mappings** (`loom.officialMojangMappings()`) — в MC 1.21.11 класс называется `Identifier` (не ResourceLocation!)

---

## Команды для проверки прогресса

```bash
# Считать файлы с ошибками компиляции
./gradlew compileJava --no-daemon 2>&1 | grep "\.java:[0-9]*: error:" | sed 's|.*/||' | sed 's/:.*//' | sort -u | wc -l

# Категоризировать оставшиеся ошибки
./gradlew compileJava --no-daemon 2>&1 | grep -A3 "error: cannot find symbol" | grep "symbol:" | sort | uniq -c | sort -rn | head -20

# Проверить что NeoForge пакеты всё ещё есть
./gradlew compileJava --no-daemon 2>&1 | grep "package net.neoforged" | sort | uniq -c | sort -rn | head -10
```

---

## Структура новых файлов (добавленных в fabric-port)

```
src/main/java/twilightforest/
├── compat/
│   ├── ArmorItem.java              # drop-in replacement для MC ArmorItem
│   └── registry/
│       ├── DeferredRegister.java   # Fabric-backed shim
│       ├── DeferredHolder.java
│       ├── DeferredBlock.java
│       └── DeferredItem.java
├── mixin/
│   ├── util/TFBeardifierAccess.java
│   ├── BeardifierMixin.java
│   ├── ChunkGeneratorMixin.java
│   ├── ChunkStatusTasksMixin.java
│   ├── LeashFenceKnotEntityMixin.java
│   ├── LevelMixin.java
│   ├── LivingEntityMixin.java
│   ├── MushroomBlockMixin.java
│   ├── NoiseChunkGeneratorMixin.java
│   ├── PathfinderMobMixin.java
│   ├── ServerEntityMixin.java
│   ├── StructureStartMixin.java
│   └── client/
│       ├── BiomeColorsMixin.java
│       ├── EntityRenderDispatcherMixin.java
│       ├── HumanoidArmorLayerMixin.java
│       └── LevelRendererMixin.java
├── client/TwilightForestClient.java
└── datagen/TFDatagenEntrypoint.java

src/main/resources/
├── fabric.mod.json
├── twilightforest.accesswidener
└── twilightforest.mixins.json
```
