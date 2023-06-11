package me.eumel.mybelovedicon;

import com.mojang.blaze3d.platform.MacosUtil;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.InputStream;

import static net.minecraft.client.Minecraft.ON_OSX;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Final private Window window;
    @Shadow @Final private VanillaPackResources vanillaPackResources;

    @Shadow public abstract ResourceManager getResourceManager();

    private static final Logger LOGGER = LoggerFactory.getLogger("my-beloved-icon");

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/MacosUtil;loadIcon(Lnet/minecraft/server/packs/resources/IoSupplier;)V"))
    private void initMacOsIcon(IoSupplier<InputStream> ioSupplier) {
        LOGGER.info("Bringing back your beloved icon since 2023!");
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;setIcon(Lnet/minecraft/server/packs/resources/IoSupplier;Lnet/minecraft/server/packs/resources/IoSupplier;)V"))
    private void initWindowsIcon(Window instance, IoSupplier<InputStream> ioSupplier, IoSupplier<InputStream> ioSupplier2) {
        LOGGER.info("Bringing back your beloved icon since 2023!");
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setIcon(GameConfig gameConfig, CallbackInfo ci) {
        try {
            ResourceManager resourceManager = this.getResourceManager();
            if (ON_OSX) {
                MacosUtil.loadIcon(() -> resourceManager.getResourceOrThrow(new ResourceLocation("my-beloved-icon", "minecraft.icns")).open());
            } else {
                this.window.setIcon(
                    () -> resourceManager.getResourceOrThrow(new ResourceLocation("my-beloved-icon", "icon_16x16.png")).open(),
                    () -> resourceManager.getResourceOrThrow(new ResourceLocation("my-beloved-icon", "icon_32x32.png")).open()
                );
            }
        } catch (Exception e) {
            LOGGER.error("Could not set icon :(", e);
        }
    }
}