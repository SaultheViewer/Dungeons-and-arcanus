package com.voltaire.d_n_a.dungeons_and_arcanus.interfaces;

import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.Tameable_Pet_With_Inv;
import net.minecraft.world.Container;

import java.util.UUID;

public interface PlayerEntityAccess {
    void openMimicInventory(Tameable_Pet_With_Inv var1, Container var2);

    void addPetMimicToOwnedList(UUID var1);

    void removePetMimicFromOwnedList(UUID var1);

    int abandonMimics();

    int getNumberOfPetMimics();

    boolean checkForMimicLimit();

    void addMimicToKeepList(UUID var1);

    void removeMimicFromKeepList(UUID var1);

    boolean isMimicInKeepList(UUID var1);
}
