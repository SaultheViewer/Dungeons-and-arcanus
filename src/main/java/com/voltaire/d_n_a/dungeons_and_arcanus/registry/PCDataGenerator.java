package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.cloudwarp.probablychests.data.PCWorldGenerator;
import org.cloudwarp.probablychests.world.feature.PCFeatures;

public class PCDataGenerator implements DataGeneratorEntrypoint {
   public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
      FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
      pack.addProvider(PCWorldGenerator::new);
   }

   public void buildRegistry(RegistrySetBuilder registryBuilder) {
      registryBuilder.add(Registries.CONFIGURED_FEATURE, PCFeatures::bootstrapConfigured);
      registryBuilder.add(Registries.PLACED_FEATURE, PCFeatures::bootstrapPlaced);
   }
}
