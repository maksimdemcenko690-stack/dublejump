package com.example.doublejump;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = DoubleJumpMod.MODID, name = "Double Jump", version = "1.0", clientSideOnly = true)
public class DoubleJumpMod {
    public static final String MODID = "doublejump";

    private static final int MAX_JUMPS = 2;

    private boolean jumpWasDown = false;
    private int airJumpsUsed = 0;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (player == null || mc.currentScreen != null) {
            jumpWasDown = false;
            return;
        }

        boolean jumpDown = mc.gameSettings.keyBindJump.isKeyDown();

        boolean resetState = player.onGround
                || player.isInWater()
                || player.isInLava()
                || player.isOnLadder()
                || player.capabilities.isFlying
                || player.isRiding();

        if (resetState) {
            airJumpsUsed = 0;
        } else if (jumpDown && !jumpWasDown && airJumpsUsed < MAX_JUMPS - 1) {
            player.motionY = 0.42D;
            airJumpsUsed++;
        }

        jumpWasDown = jumpDown;
    }
}
