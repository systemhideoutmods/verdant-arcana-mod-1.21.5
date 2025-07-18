package net.systemhideout.verdantarcana.block.entity.custom;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.systemhideout.verdantarcana.block.entity.ImplementedInventory;
import net.systemhideout.verdantarcana.block.entity.ModBlockEntities;
import net.systemhideout.verdantarcana.recipe.WitchAltarRecipe;
import net.systemhideout.verdantarcana.recipe.WitchAltarRecipeInput;
import net.systemhideout.verdantarcana.recipe.ModRecipes;
import net.systemhideout.verdantarcana.screen.custom.WitchAltarScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WitchAltarBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> lastInputs = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private static final int INPUT_SLOT_0 = 0;
    private static final int INPUT_SLOT_1 = 1;
    private static final int INPUT_SLOT_2 = 2;
    private static final int INPUT_SLOT_3 = 3;
    private static final int OUTPUT_SLOT = 4;


    private final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 100;

    public WitchAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WITCH_ALTAR, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> WitchAltarBlockEntity.this.progress;
                    case 1 -> WitchAltarBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> WitchAltarBlockEntity.this.progress = value;
                    case 1 -> WitchAltarBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.verdant-arcana-mod.witch_altar");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new WitchAltarScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        ItemScatterer.spawn(world, pos, this);
        super.onBlockReplaced(pos, oldState);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putInt("witch_altar.progress", progress);
        nbt.putInt("witch_altar.max_progress", maxProgress);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        progress = nbt.getInt("witch_altar.progress").get();
        maxProgress = nbt.getInt("witch_altar.max_progress").get();
        super.readNbt(nbt, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasNonEmptyInput() && hasRecipe()) {
            increaseCraftingProgress();
            markDirty(world, pos, state);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasNonEmptyInput() {
        return !inventory.get(INPUT_SLOT_0).isEmpty()
                || !inventory.get(INPUT_SLOT_1).isEmpty()
                || !inventory.get(INPUT_SLOT_2).isEmpty()
                || !inventory.get(INPUT_SLOT_3).isEmpty();
    }

    private void craftItem() {
        Optional<RecipeEntry<WitchAltarRecipe>> recipe = getCurrentRecipe();

        ItemStack output = recipe.get().value().output();
        this.removeStack(INPUT_SLOT_0, 1);
        this.removeStack(INPUT_SLOT_1, 1);
        this.removeStack(INPUT_SLOT_2, 1);
        this.removeStack(INPUT_SLOT_3, 1);
        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(),
                this.getStack(OUTPUT_SLOT).getCount() + output.getCount()));
    }


    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        this.progress++;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<WitchAltarRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeEntry<WitchAltarRecipe>> getCurrentRecipe() {
        DefaultedList<ItemStack> inputs = DefaultedList.ofSize(4, ItemStack.EMPTY);
        inputs.set(0, this.getStack(INPUT_SLOT_0));
        inputs.set(1, this.getStack(INPUT_SLOT_1));
        inputs.set(2, this.getStack(INPUT_SLOT_2));
        inputs.set(3, this.getStack(INPUT_SLOT_3));

        return ((ServerWorld) this.getWorld()).getRecipeManager()
                .getFirstMatch(ModRecipes.WITCH_ALTAR_TYPE, new WitchAltarRecipeInput(inputs), this.getWorld());
    }


    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = this.getStack(OUTPUT_SLOT).isEmpty() ? 64 : this.getStack(OUTPUT_SLOT).getMaxCount();
        int currentCount = this.getStack(OUTPUT_SLOT).getCount();
        return maxCount >= currentCount + count;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
