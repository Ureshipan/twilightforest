# Twilight Forest → Fabric Port Journal

**Цель:** порт с NeoForge 21.4.124 / MC 1.21.4 → чистый Fabric 0.141.4 / MC 1.21.11  
**Ветка:** `fabric-port`  
**Стратегия:** «compile-first» — сначала добиться компиляции, потом рантайм

---

## Текущее состояние компиляции

Последний замер: **~35 файлов с ошибками** (было 41, стало меньше после серии фиксов).  
Ошибки делятся на категории (оставшееся):

| Категория | Количество | Статус |
|-----------|-----------|--------|
| `PartEntity` (NeoForge) | ~3 | ⏳ Нужен TFPartEntity |
| `Component` (chat) | ~3 | ⏳ Пропавший импорт |
| `SimpleInstance`/`SimpleCriterionTrigger`/`ContextAwarePredicate` | ~6 | ⏳ MC API change |
| `ItemStackHandler` (NeoForge capabilities) | ~1 | ⏳ Фаза 5 |
| NeoForge event bus | ~22 файла | ⏳ Фаза 2 |
| NeoForge networking | ~14 файлов | ⏳ Фаза 3 |
| NeoForge capabilities | ~2 файла | ⏳ Фаза 5 |
| Beanification `@Autowired` | во многих | ⏳ Нужно удалить |

---

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
