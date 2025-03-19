package com.chailotl.pumpkin_pie;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModCreativeTabs;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.refabricated.LootModificationEvents;

import static vectorwing.farmersdelight.common.registry.ModItems.*;

public class Main implements ModInitializer
{
	public static final Item PUMPKIN_PIE_SLICE;
	public static final Block PUMPKIN_PIE;
	public static final Item PUMPKIN_PIE_ITEM;

	private static final RegistryKey<LootTable> BLOCKS_PUMPKIN_PIE = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(FarmersDelight.MODID, "blocks/pumpkin_pie"));

	static
	{
		PUMPKIN_PIE_SLICE = Registry.register(Registries.ITEM, Identifier.of(FarmersDelight.MODID, "pumpkin_pie_slice"), new Item(foodItem(FoodValues.PIE_SLICE)));
		PUMPKIN_PIE = Registry.register(Registries.BLOCK, Identifier.of(FarmersDelight.MODID, "pumpkin_pie"), new PumpkinPieBlock(AbstractBlock.Settings.copy(Blocks.CAKE), () -> PUMPKIN_PIE_SLICE));
		PUMPKIN_PIE_ITEM = Registry.register(Registries.ITEM, Identifier.of(FarmersDelight.MODID, "pumpkin_pie"), new BlockItem(PUMPKIN_PIE, basicItem()));
	}

	@Override
	public void onInitialize() {
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(PUMPKIN_PIE_SLICE, 0.85F);

		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (key == BLOCKS_PUMPKIN_PIE)
				LootModificationEvents.pastrySlicing(tableBuilder, PUMPKIN_PIE, PUMPKIN_PIE_SLICE, PumpkinPieBlock.BITES, 4);
		});

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			var key = Registries.ITEM_GROUP.getKey(ModCreativeTabs.TAB_FARMERS_DELIGHT.get()).get();

			ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
				content.addAfter(ModItems.CAKE_SLICE.get(), PUMPKIN_PIE_SLICE);
			});
		});
	}
}