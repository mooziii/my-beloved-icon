package me.eumel.mybelovedicon;

import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.VanillaPackResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow @Final private Window window;
    @Shadow @Final private VanillaPackResources vanillaPackResources;
    private static final Logger LOGGER = LoggerFactory.getLogger("my-beloved-icon");

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;setIcon(Lnet/minecraft/server/packs/PackResources;Lcom/mojang/blaze3d/platform/IconSet;)V"))
    private void init(Window instance, PackResources packResources, IconSet iconSet) {
        LOGGER.info("Bringing back your beloved icon since 2023!");
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setIcon(GameConfig gameConfig, CallbackInfo ci) {
        try {
            window.setIcon(vanillaPackResources, IconSet.RELEASE);
        } catch (Exception e) {
            LOGGER.error("Could not set icon :(", e);
        }
    }
}