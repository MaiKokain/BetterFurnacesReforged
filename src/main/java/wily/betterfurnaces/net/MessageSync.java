package wily.betterfurnaces.net;

import java.nio.charset.StandardCharsets;

import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.handler.GuiBF;
import wily.betterfurnaces.handler.GuiForgeBF;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSync implements IMessage {

    protected TileEntitySmeltingBase te;
    protected int[] fromNet;
    protected FluidStack stack;

    // Number of leading ints before the slotCookTime[] array: energy, burnTime, fuelLength
    private static final int FIXED_INTS = 3;

    public MessageSync() {}

    public MessageSync(TileEntitySmeltingBase te) {
        this.te = te;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // Need to know how many slots this TE has. Client-side TE is the target.
        int slotCount = 1;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiBF) {
            slotCount = ((GuiBF) Minecraft.getMinecraft().currentScreen).getTE().getSlotCookTimes().length;
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiForgeBF) {
            slotCount = ((GuiForgeBF) Minecraft.getMinecraft().currentScreen).getTE().getSlotCookTimes().length;
        }

        fromNet = new int[FIXED_INTS + slotCount];
        for (int i = 0; i < fromNet.length; i++)
            fromNet[i] = buf.readInt();

        String name = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
        if (!name.equals("null")) {
            Fluid fluid = FluidRegistry.getFluid(name);
            if (fluid != null) {
                stack = new FluidStack(fluid, buf.readInt());
            } else {
                buf.readInt(); // consume amount so the buffer stays aligned
                stack = null;
                BetterFurnacesReforged.LOGGER.warn("Unknown fluid '{}' in sync packet; ignoring.", name);
            }
        } else {
            stack = null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        te.writeContainerSync(buf);
    }

    public static class Handler implements IMessageHandler<MessageSync, IMessage> {
        @Override
        public IMessage onMessage(MessageSync message, MessageContext ctx) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiBF) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ((GuiBF) Minecraft.getMinecraft().currentScreen).getTE()
                            .readContainerSync(message.fromNet, message.stack);
                });
            }
            if (Minecraft.getMinecraft().currentScreen instanceof GuiForgeBF) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ((GuiForgeBF) Minecraft.getMinecraft().currentScreen).getTE()
                            .readContainerSync(message.fromNet, message.stack);
                });
            }
            return null;
        }
    }
}