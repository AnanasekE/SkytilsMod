/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2021 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package skytils.skytilsmod.mixins.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skytils.skytilsmod.Skytils;
import skytils.skytilsmod.utils.RenderUtilKt;

import java.awt.*;

import static skytils.skytilsmod.features.impl.dungeons.solvers.BlazeSolver.blazeMode;
import static skytils.skytilsmod.features.impl.dungeons.solvers.BlazeSolver.orderedBlazes;

@Mixin(ModelBlaze.class)
public abstract class MixinModelBlaze extends ModelBase {


    @Inject(method = "render", at = @At(value = "HEAD"))
    private void changeBlazeColor(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci) {
        if (orderedBlazes.size() == 0) return;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (blazeMode <= 0) {
            if (entity.isEntityEqual(orderedBlazes.get(0).blaze)) {
                Color color = Skytils.config.lowestBlazeColor;
                RenderUtilKt.bindColor(color);
            } else if (Skytils.config.showNextBlaze && orderedBlazes.size() > 1 && entity.isEntityEqual(orderedBlazes.get(1).blaze) && blazeMode != 0) {
                Color color = Skytils.config.nextBlazeColor;
                RenderUtilKt.bindColor(color);
            }
        }
        if (blazeMode >= 0) {
            if (entity.isEntityEqual(orderedBlazes.get(orderedBlazes.size() - 1).blaze)) {
                Color color = Skytils.config.highestBlazeColor;
                RenderUtilKt.bindColor(color);
            } else if (Skytils.config.showNextBlaze && orderedBlazes.size() > 1 && entity.isEntityEqual(orderedBlazes.get(orderedBlazes.size() - 2).blaze) && blazeMode != 0) {
                Color color = Skytils.config.nextBlazeColor;
                RenderUtilKt.bindColor(color);
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void renderPost(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci) {
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
}
