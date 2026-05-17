package com.eon.lazy_patch.data;

import com.eon.lazy_patch.EonLazyPatch;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class EonDataGenerators {
    private EonDataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        event.addProvider(new DataJsonProvider(event.getGenerator().getPackOutput()));
        event.addProvider(new AssetJsonProvider(event.getGenerator().getPackOutput()));
    }

    private static final class DataJsonProvider implements DataProvider {
        private final PackOutput output;
        private final Map<String, JsonElement> files = new LinkedHashMap<>();

        private DataJsonProvider(PackOutput output) {
            this.output = output;
            addRecipes();
            addLootTables();
            addTags();
        }

        private void addRecipes() {
            add("eon_lazy_patch/recipe/experience_crystal.json", """
                    {
                      "type": "minecraft:crafting_shaped",
                      "category": "misc",
                      "pattern": ["DAD", "BEB", "DAD"],
                      "key": {
                        "D": {"item": "minecraft:diamond"},
                        "A": {"item": "minecraft:amethyst_shard"},
                        "B": {"item": "minecraft:glass"},
                        "E": {"item": "minecraft:emerald_block"}
                      },
                      "result": {"id": "eon_lazy_patch:experience_crystal", "count": 1}
                    }
                    """);
            add("eon_lazy_patch/recipe/experience_infuser.json", """
                    {
                      "type": "minecraft:crafting_shaped",
                      "category": "misc",
                      "pattern": ["GAG", "IEI", "GHG"],
                      "key": {
                        "G": {"item": "minecraft:glass"},
                        "A": {"item": "minecraft:amethyst_shard"},
                        "I": {"item": "minecraft:iron_ingot"},
                        "E": {"item": "minecraft:emerald_block"},
                        "H": {"item": "minecraft:hopper"}
                      },
                      "result": {"id": "eon_lazy_patch:experience_infuser", "count": 1}
                    }
                    """);
            add("eon_lazy_patch/recipe/constant_generator.json", customMekanismRecipe("charged_energy_tablet_constant_generator"));
            add("eon_lazy_patch/recipe/creative_energy_cube.json", customMekanismRecipe("creative_energy_cube"));

            addCalibrationCore("liquid_sculk_matter", "ifeu:sculk_gear", true);
            addCalibrationCore("liquid_dragon_breath", "ifeu:liquid_dragon_breath_bucket", true);
            addCalibrationCore("ether_gas", "minecraft:nether_star", false);
            addCalibrationCore("pink_slime", "industrialforegoing:pink_slime_bucket", false);

            addInfinityCell("liquid_sculk_matter", true);
            addInfinityCell("liquid_dragon_breath", true);
            addInfinityCell("ether_gas", false);
            addInfinityCell("pink_slime", false);
        }

        private String customMekanismRecipe(String serializerPath) {
            return """
                    {
                      "neoforge:conditions": [
                        {"type": "neoforge:mod_loaded", "modid": "mekanism"}
                      ],
                      "type": "eon_lazy_patch:%s",
                      "category": "misc"
                    }
                    """.formatted(serializerPath);
        }

        private void addCalibrationCore(String name, String themeItem, boolean needsIfeu) {
            String optionalCondition = needsIfeu
                    ? """
                          ,
                            {"type": "neoforge:mod_loaded", "modid": "ifeu"}
                        """
                    : "";
            add("eon_lazy_patch/recipe/ae2/%s_calibration_core.json".formatted(name), """
                    {
                      "neoforge:conditions": [
                        {"type": "neoforge:mod_loaded", "modid": "ae2"},
                        {"type": "neoforge:mod_loaded", "modid": "industrialforegoing"}%s
                      ],
                      "type": "minecraft:crafting_shaped",
                      "category": "misc",
                      "pattern": ["DMD", "PCP", "DFD"],
                      "key": {
                        "D": {"item": "minecraft:diamond"},
                        "M": {"item": "industrialforegoing:machine_frame_advanced"},
                        "P": {"item": "industrialforegoing:pink_slime_ingot"},
                        "C": {"item": "%s"},
                        "F": {"item": "ae2:cell_component_64k"}
                      },
                      "result": {"id": "eon_lazy_patch:ae2/%s_calibration_core", "count": 1}
                    }
                    """.formatted(optionalCondition, themeItem, name));
        }

        private void addInfinityCell(String name, boolean needsIfeu) {
            String fluidModId = needsIfeu ? "ifeu" : "industrialforegoing";
            String industrialForegoingCondition = needsIfeu
                    ? """
                          ,
                            {"type": "neoforge:mod_loaded", "modid": "industrialforegoing"}
                        """
                    : "";
            add("eon_lazy_patch/recipe/ae2/infinity_%s_cell.json".formatted(name), """
                    {
                      "neoforge:conditions": [
                        {"type": "neoforge:mod_loaded", "modid": "ae2"},
                        {"type": "neoforge:mod_loaded", "modid": "%s"}%s
                      ],
                      "type": "minecraft:crafting_shapeless",
                      "category": "misc",
                      "ingredients": [
                        {"item": "ae2:fluid_cell_housing"},
                        {"item": "eon_lazy_patch:ae2/%s_calibration_core"}
                      ],
                      "result": {"id": "eon_lazy_patch:ae2/infinity_%s_cell", "count": 1}
                    }
                    """.formatted(fluidModId, industrialForegoingCondition, name, name));
        }

        private void addLootTables() {
            addBlockDrop("experience_infuser");
            addBlockDrop("constant_generator");
        }

        private void addBlockDrop(String name) {
            add("eon_lazy_patch/loot_table/blocks/%s.json".formatted(name), """
                    {
                      "type": "minecraft:block",
                      "pools": [
                        {
                          "rolls": 1,
                          "entries": [
                            {"type": "minecraft:item", "name": "eon_lazy_patch:%s"}
                          ],
                          "conditions": [
                            {"condition": "minecraft:survives_explosion"}
                          ]
                        }
                      ]
                    }
                    """.formatted(name));
        }

        private void addTags() {
            add("minecraft/tags/block/mineable/pickaxe.json", """
                    {
                      "replace": false,
                      "values": [
                        "eon_lazy_patch:experience_infuser",
                        "eon_lazy_patch:constant_generator"
                      ]
                    }
                    """);
        }

        private void add(String path, String json) {
            files.put(path, JsonParser.parseString(json));
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            Path root = output.getOutputFolder(PackOutput.Target.DATA_PACK);
            return CompletableFuture.allOf(files.entrySet().stream()
                    .map(entry -> DataProvider.saveStable(cache, entry.getValue(), root.resolve(entry.getKey())))
                    .toArray(CompletableFuture[]::new));
        }

        @Override
        public String getName() {
            return "Eon Lazy Patch Data JSON";
        }
    }

    private static final class AssetJsonProvider implements DataProvider {
        private final PackOutput output;
        private final Map<String, JsonElement> files = new LinkedHashMap<>();

        private AssetJsonProvider(PackOutput output) {
            this.output = output;
            addLang();
            addModels();
            addBlockStates();
        }

        private void addLang() {
            add("lang/en_us.json", """
                    {
                      "item.eon_lazy_patch.misc.eon_star": "Eon Star",
                      "item.eon_lazy_patch.experience_crystal": "Experience Crystal",
                      "block.eon_lazy_patch.experience_infuser": "Experience Infuser",
                      "item.eon_lazy_patch.constant_generator": "Constant Generator",
                      "block.eon_lazy_patch.constant_generator": "Constant Generator",
                      "item.eon_lazy_patch.ae2.infinity_liquid_sculk_matter_cell": "ME Infinity Liquid Sculk Matter Cell",
                      "item.eon_lazy_patch.ae2.infinity_liquid_dragon_breath_cell": "ME Infinity Liquid Dragon Breath Cell",
                      "item.eon_lazy_patch.ae2.infinity_ether_gas_cell": "ME Infinity Ether Gas Cell",
                      "item.eon_lazy_patch.ae2.infinity_pink_slime_cell": "ME Infinity Pink Slime Cell",
                      "item.eon_lazy_patch.ae2.liquid_sculk_matter_calibration_core": "Liquid Sculk Matter Calibration Core",
                      "item.eon_lazy_patch.ae2.liquid_dragon_breath_calibration_core": "Liquid Dragon Breath Calibration Core",
                      "item.eon_lazy_patch.ae2.ether_gas_calibration_core": "Ether Gas Calibration Core",
                      "item.eon_lazy_patch.ae2.pink_slime_calibration_core": "Pink Slime Calibration Core",
                      "item.eon_lazy_patch.infinity_fluid_cell": "ME Infinity %s Cell",
                      "itemGroup.eon_lazy_patch": "Eon Lazy Patch",
                      "tooltip.eon_lazy_patch.infinity_fluid_cell": "Provides an effectively infinite amount of its recorded fluid to ME networks.",
                      "tooltip.eon_lazy_patch.experience_crystal.stored": "Stored XP: %s / %s",
                      "tooltip.eon_lazy_patch.experience_crystal.use": "Use: withdraw up to 30 levels worth of XP.",
                      "tooltip.eon_lazy_patch.experience_crystal.sneak_use": "Sneak-use: store up to 30 levels worth of XP.",
                      "tooltip.eon_lazy_patch.experience_infuser.fluid": "Experience Fluid",
                      "tooltip.eon_lazy_patch.experience_infuser.empty": "Empty",
                      "tooltip.eon_lazy_patch.constant_generator.energy": "Stored Energy",
                      "eon_lazy_patch.configuration.title": "Eon Lazy Patch Configs",
                      "eon_lazy_patch.configuration.section.eon_lazy_patch.common.toml": "Eon Lazy Patch Configs",
                      "eon_lazy_patch.configuration.section.eon_lazy_patch.common.toml.title": "Eon Lazy Patch Configs",
                      "eon_lazy_patch.configuration.items": "Item List",
                      "eon_lazy_patch.configuration.logDirtBlock": "Log Dirt Block",
                      "eon_lazy_patch.configuration.magicNumberIntroduction": "Magic Number Text",
                      "eon_lazy_patch.configuration.magicNumber": "Magic Number"
                    }
                    """);
            add("lang/zh_cn.json", """
                    {
                      "item.eon_lazy_patch.misc.eon_star": "Eon 之星",
                      "item.eon_lazy_patch.experience_crystal": "经验水晶",
                      "block.eon_lazy_patch.experience_infuser": "经验灌注器",
                      "item.eon_lazy_patch.constant_generator": "恒能发电机",
                      "block.eon_lazy_patch.constant_generator": "恒能发电机",
                      "item.eon_lazy_patch.ae2.infinity_liquid_sculk_matter_cell": "ME 无限液态幽匿物质元件",
                      "item.eon_lazy_patch.ae2.infinity_liquid_dragon_breath_cell": "ME 无限液态龙息元件",
                      "item.eon_lazy_patch.ae2.infinity_ether_gas_cell": "ME 无限以太气体元件",
                      "item.eon_lazy_patch.ae2.infinity_pink_slime_cell": "ME 无限粉色史莱姆液元件",
                      "item.eon_lazy_patch.ae2.liquid_sculk_matter_calibration_core": "液态幽匿物质校准核心",
                      "item.eon_lazy_patch.ae2.liquid_dragon_breath_calibration_core": "液态龙息校准核心",
                      "item.eon_lazy_patch.ae2.ether_gas_calibration_core": "以太气体校准核心",
                      "item.eon_lazy_patch.ae2.pink_slime_calibration_core": "粉色史莱姆液校准核心",
                      "item.eon_lazy_patch.infinity_fluid_cell": "ME 无限%s元件",
                      "itemGroup.eon_lazy_patch": "Eon 懒人补丁",
                      "tooltip.eon_lazy_patch.infinity_fluid_cell": "向 ME 网络提供近乎无限的指定流体。",
                      "tooltip.eon_lazy_patch.experience_crystal.stored": "储存经验：%s / %s",
                      "tooltip.eon_lazy_patch.experience_crystal.use": "右键：取出最多 30 级跨度的经验。",
                      "tooltip.eon_lazy_patch.experience_crystal.sneak_use": "潜行右键：存入最多 30 级跨度的经验。",
                      "tooltip.eon_lazy_patch.experience_infuser.fluid": "经验流体",
                      "tooltip.eon_lazy_patch.experience_infuser.empty": "空",
                      "tooltip.eon_lazy_patch.constant_generator.energy": "储能",
                      "eon_lazy_patch.configuration.title": "Eon Lazy Patch 配置",
                      "eon_lazy_patch.configuration.section.eon_lazy_patch.common.toml": "Eon Lazy Patch 配置",
                      "eon_lazy_patch.configuration.section.eon_lazy_patch.common.toml.title": "Eon Lazy Patch 配置",
                      "eon_lazy_patch.configuration.items": "物品列表",
                      "eon_lazy_patch.configuration.logDirtBlock": "记录泥土方块",
                      "eon_lazy_patch.configuration.magicNumberIntroduction": "魔法数字文本",
                      "eon_lazy_patch.configuration.magicNumber": "魔法数字"
                    }
                    """);
        }

        private void addModels() {
            addGeneratedItem("experience_crystal", "eon_lazy_patch:item/experience_crystal");
            addGeneratedItem("misc/eon_star", "eon_lazy_patch:item/misc/eon_star");
            addBlockItem("experience_infuser");
            addBlockItem("constant_generator");

            addAe2Generated("liquid_sculk_matter_calibration_core");
            addAe2Generated("liquid_dragon_breath_calibration_core");
            addAe2Generated("ether_gas_calibration_core");
            addAe2Generated("pink_slime_calibration_core");

            addInfinityCellModel("liquid_sculk_matter");
            addInfinityCellModel("liquid_dragon_breath");
            addInfinityCellModel("ether_gas");
            addInfinityCellModel("pink_slime");

            add("models/block/experience_infuser.json", """
                    {
                      "parent": "minecraft:block/cube",
                      "textures": {
                        "down": "eon_lazy_patch:block/experience_infuser/bottom",
                        "up": "eon_lazy_patch:block/experience_infuser/top",
                        "north": "eon_lazy_patch:block/experience_infuser/side",
                        "south": "eon_lazy_patch:block/experience_infuser/side",
                        "east": "eon_lazy_patch:block/experience_infuser/side",
                        "west": "eon_lazy_patch:block/experience_infuser/side",
                        "particle": "eon_lazy_patch:block/experience_infuser/side"
                      }
                    }
                    """);
            add("models/block/constant_generator.json", """
                    {
                      "parent": "minecraft:block/cube",
                      "textures": {
                        "down": "eon_lazy_patch:block/experience_infuser/bottom",
                        "up": "eon_lazy_patch:block/experience_infuser/top",
                        "north": "eon_lazy_patch:block/constant_generator/front",
                        "south": "eon_lazy_patch:block/constant_generator/side",
                        "east": "eon_lazy_patch:block/constant_generator/side",
                        "west": "eon_lazy_patch:block/constant_generator/side",
                        "particle": "eon_lazy_patch:block/constant_generator/side"
                      }
                    }
                    """);
        }

        private void addGeneratedItem(String path, String texture) {
            add("models/item/%s.json".formatted(path), """
                    {
                      "parent": "minecraft:item/generated",
                      "textures": {"layer0": "%s"}
                    }
                    """.formatted(texture));
        }

        private void addBlockItem(String name) {
            add("models/item/%s.json".formatted(name), """
                    {
                      "parent": "eon_lazy_patch:block/%s"
                    }
                    """.formatted(name));
        }

        private void addAe2Generated(String name) {
            addGeneratedItem("ae2/%s".formatted(name), "eon_lazy_patch:item/ae2/%s".formatted(name));
        }

        private void addInfinityCellModel(String name) {
            add("models/item/ae2/infinity_%s_cell.json".formatted(name), """
                    {
                      "parent": "minecraft:item/generated",
                      "textures": {
                        "layer0": "ae2:item/fluid_storage_cell_256k",
                        "layer1": "ae2:item/storage_cell_led"
                      }
                    }
                    """);
            add("models/block/drive/infinity_%s_cell.json".formatted(name), """
                    {
                      "credit": "Based on AE2 drive cell models",
                      "ambientocclusion": false,
                      "textures": {
                        "particle": "ae2:block/drive/drive_cells",
                        "cell": "ae2:block/drive/drive_cells"
                      },
                      "elements": [
                        {
                          "name": "Cell Backdrop",
                          "from": [0, 0, 0],
                          "to": [6, 2, 2],
                          "rotation": {"angle": 0, "axis": "y", "origin": [9, 8, 8]},
                          "faces": {
                            "north": {"uv": [6, 8, 12, 10], "texture": "#cell", "cullface": "north"},
                            "up": {"uv": [12, 8, 6, 10], "texture": "#cell", "cullface": "north"},
                            "down": {"uv": [12, 8, 6, 10], "texture": "#cell", "cullface": "north"}
                          }
                        }
                      ]
                    }
                    """);
        }

        private void addBlockStates() {
            add("blockstates/experience_infuser.json", """
                    {
                      "variants": {
                        "": {"model": "eon_lazy_patch:block/experience_infuser"}
                      }
                    }
                    """);
            add("blockstates/constant_generator.json", """
                    {
                      "variants": {
                        "facing=north": {"model": "eon_lazy_patch:block/constant_generator"},
                        "facing=east": {"model": "eon_lazy_patch:block/constant_generator", "y": 90},
                        "facing=south": {"model": "eon_lazy_patch:block/constant_generator", "y": 180},
                        "facing=west": {"model": "eon_lazy_patch:block/constant_generator", "y": 270}
                      }
                    }
                    """);
        }

        private void add(String path, String json) {
            files.put(path, JsonParser.parseString(json));
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            Path root = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(EonLazyPatch.MODID);
            return CompletableFuture.allOf(files.entrySet().stream()
                    .map(entry -> DataProvider.saveStable(cache, entry.getValue(), root.resolve(entry.getKey())))
                    .toArray(CompletableFuture[]::new));
        }

        @Override
        public String getName() {
            return "Eon Lazy Patch Asset JSON";
        }
    }
}
