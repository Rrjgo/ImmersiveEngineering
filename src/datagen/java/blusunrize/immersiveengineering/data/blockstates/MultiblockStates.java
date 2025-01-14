package blusunrize.immersiveengineering.data.blockstates;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.multiblocks.TemplateMultiblock;
import blusunrize.immersiveengineering.client.models.obj.callback.block.BottlingMachineCallbacks;
import blusunrize.immersiveengineering.client.models.obj.callback.block.ClocheCallbacks;
import blusunrize.immersiveengineering.client.models.obj.callback.block.WorkbenchCallbacks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IEMultiblocks;
import blusunrize.immersiveengineering.common.blocks.multiblocks.IETemplateMultiblock;
import blusunrize.immersiveengineering.common.blocks.wooden.ModWorkbenchBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks.MetalDevices;
import blusunrize.immersiveengineering.common.register.IEBlocks.Multiblocks;
import blusunrize.immersiveengineering.common.register.IEBlocks.WoodenDevices;
import blusunrize.immersiveengineering.data.models.NongeneratedModels.NongeneratedModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static blusunrize.immersiveengineering.ImmersiveEngineering.rl;

public class MultiblockStates extends ExtendedBlockstateProvider
{
	private static final List<Vec3i> CUBE_THREE = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
			.map(BlockPos::immutable)
			.collect(Collectors.toList());
	private static final List<Vec3i> CUBE_TWO = BlockPos.betweenClosedStream(0, 0, -1, 1, 1, 0)
			.map(BlockPos::immutable)
			.collect(Collectors.toList());

	public final Map<Block, ModelFile> unsplitModels = new HashMap<>();
	public ModelFile blastFurnaceOff;
	public ModelFile blastFurnaceOn;
	public ModelFile cokeOvenOff;
	public ModelFile cokeOvenOn;
	public ModelFile alloySmelterOff;
	public ModelFile alloySmelterOn;

	public MultiblockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels()
	{
		createStoneMultiblocks();
		createMetalMultiblocks();

		createMultiblock(
				MetalDevices.CLOCHE,
				splitDynamic(
						ieObjBuilder("block/metal_device/cloche.obj.ie", innerModels)
								.callback(ClocheCallbacks.INSTANCE)
								.end(),
						COLUMN_THREE
				)
		);
		createMultiblock(
				MetalDevices.TESLA_COIL,
				split(
						innerObj("block/metal_device/teslacoil.obj"),
						ImmutableList.of(BlockPos.ZERO, new BlockPos(0, 0, -1))
				),
				null, IEProperties.FACING_ALL, null
		);
		createMultiblock(
				MetalDevices.BLAST_FURNACE_PREHEATER,
				split(innerObj("block/metal_device/blastfurnace_preheater.obj"), COLUMN_THREE)
		);
	}

	private void createStoneMultiblocks()
	{
		blastFurnaceOff = cubeThree("blast_furnace_off",
				modLoc("block/multiblocks/blast_furnace"),
				modLoc("block/multiblocks/blast_furnace_off")
		);
		blastFurnaceOn = cubeThree("blast_furnace_on",
				modLoc("block/multiblocks/blast_furnace"),
				modLoc("block/multiblocks/blast_furnace_on")
		);
		cokeOvenOff = cubeThree("coke_oven_off",
				modLoc("block/multiblocks/coke_oven"),
				modLoc("block/multiblocks/coke_oven_off")
		);
		cokeOvenOn = cubeThree("coke_oven_on",
				modLoc("block/multiblocks/coke_oven"),
				modLoc("block/multiblocks/coke_oven_on")
		);
		alloySmelterOff = cubeTwo("alloy_smelter_off",
				modLoc("block/multiblocks/alloy_smelter_top"),
				modLoc("block/multiblocks/alloy_smelter_bottom"),
				modLoc("block/multiblocks/alloy_smelter_side"),
				modLoc("block/multiblocks/alloy_smelter_off")
		);
		alloySmelterOn = cubeTwo("alloy_smelter_on",
				modLoc("block/multiblocks/alloy_smelter_top"),
				modLoc("block/multiblocks/alloy_smelter_bottom"),
				modLoc("block/multiblocks/alloy_smelter_side"),
				modLoc("block/multiblocks/alloy_smelter_on")
		);
		createMultiblock(Multiblocks.COKE_OVEN, cokeOvenOff, cokeOvenOn,
				IEProperties.ACTIVE);
		createMultiblock(Multiblocks.ALLOY_SMELTER, alloySmelterOff, alloySmelterOn,
				IEProperties.ACTIVE);
		createMultiblock(Multiblocks.BLAST_FURNACE, blastFurnaceOff, blastFurnaceOn,
				IEProperties.ACTIVE);
	}

	private void createMetalMultiblocks()
	{
		createMultiblock(innerObj("block/metal_multiblock/sawmill.obj"), IEMultiblocks.SAWMILL);
		createMultiblock(innerObj("block/metal_multiblock/excavator.obj"), IEMultiblocks.EXCAVATOR);
		createMultiblock(innerObj("block/metal_multiblock/crusher.obj"), IEMultiblocks.CRUSHER);
		createMultiblock(Multiblocks.METAL_PRESS, split(
				innerObj("block/metal_multiblock/metal_press.obj"),
				IEMultiblocks.METAL_PRESS,
				p -> new BlockPos(p.getZ()+1, p.getY(), p.getX()-1),
				false
		));
		createMultiblock(Multiblocks.ASSEMBLER,
				split(innerObj("block/metal_multiblock/assembler.obj"), IEMultiblocks.ASSEMBLER));
		createMultiblock(innerObj("block/metal_multiblock/arc_furnace.obj"), IEMultiblocks.ARC_FURNACE);

		createMultiblock(Multiblocks.ADVANCED_BLAST_FURNACE, split(innerObj("block/blastfurnace_advanced.obj"), IEMultiblocks.ADVANCED_BLAST_FURNACE));
		createMultiblock(Multiblocks.SILO, split(innerObj("block/metal_multiblock/silo.obj"), IEMultiblocks.SILO));
		createMultiblock(Multiblocks.TANK, split(innerObj("block/metal_multiblock/tank.obj"), IEMultiblocks.SHEETMETAL_TANK));
		createDynamicMultiblock(
				ieObjBuilder("block/metal_multiblock/bottling_machine.obj.ie", innerModels).callback(BottlingMachineCallbacks.INSTANCE).end(),
				IEMultiblocks.BOTTLING_MACHINE
		);
		createMultiblock(innerObj("block/metal_multiblock/fermenter.obj"), IEMultiblocks.FERMENTER);
		createMultiblock(innerObj("block/metal_multiblock/squeezer.obj"), IEMultiblocks.SQUEEZER);
		createMultiblock(innerObj("block/metal_multiblock/mixer.obj"), IEMultiblocks.MIXER);
		createMultiblock(innerObj("block/metal_multiblock/refinery.obj"), IEMultiblocks.REFINERY);
		createMultiblock(innerObj("block/metal_multiblock/diesel_generator.obj"), IEMultiblocks.DIESEL_GENERATOR);
		createMultiblock(Multiblocks.LIGHTNING_ROD,
				split(innerObj("block/metal_multiblock/lightningrod.obj"), IEMultiblocks.LIGHTNING_ROD));
		createMultiblock(WoodenDevices.WORKBENCH,
				splitDynamic(
						ieObjBuilder("block/wooden_device/workbench.obj.ie", innerModels).callback(WorkbenchCallbacks.INSTANCE).end(),
						ImmutableList.of(ModWorkbenchBlockEntity.MASTER_POS, ModWorkbenchBlockEntity.DUMMY_POS)
				),
				null, null);
		createMultiblock(WoodenDevices.CIRCUIT_TABLE,
				split(innerObj("block/wooden_device/circuit_table.obj"), ImmutableList.of(
						ModWorkbenchBlockEntity.MASTER_POS, ModWorkbenchBlockEntity.DUMMY_POS
				)),
				null, null);
		createMultiblock(MetalDevices.SAMPLE_DRILL,
				split(
						innerObj("block/metal_device/core_drill.obj"),
						ImmutableList.of(BlockPos.ZERO, BlockPos.ZERO.above(), BlockPos.ZERO.above(2))
				),
				null, null);
		createMultiblock(innerObj("block/metal_multiblock/auto_workbench.obj"), IEMultiblocks.AUTO_WORKBENCH);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Multiblock models/block states";
	}

	private ModelFile cubeTwo(String name, ResourceLocation top, ResourceLocation bottom,
							  ResourceLocation side, ResourceLocation front)
	{
		NongeneratedModel baseModel = obj(name, rl("block/stone_multiblocks/cube_two.obj"),
				ImmutableMap.<String, ResourceLocation>builder()
						.put("side", side)
						.put("top", top)
						.put("bottom", bottom)
						.put("front", front)
						.build(),
				innerModels
		);
		return splitModel(name+"_split", baseModel, CUBE_TWO, false);
	}

	private ModelFile cubeThree(String name, ResourceLocation def, ResourceLocation front)
	{
		NongeneratedModel baseModel = obj(name, rl("block/stone_multiblocks/cube_three.obj"),
				ImmutableMap.of("side", def, "front", front), innerModels);
		return splitModel(name+"_split", baseModel, CUBE_THREE, false);
	}

	private void createMultiblock(NongeneratedModel unsplitModel, IETemplateMultiblock multiblock)
	{
		createMultiblock(unsplitModel, multiblock, false);
	}

	private void createDynamicMultiblock(NongeneratedModel unsplitModel, IETemplateMultiblock multiblock)
	{
		createMultiblock(unsplitModel, multiblock, true);
	}

	private void createMultiblock(NongeneratedModel unsplitModel, IETemplateMultiblock multiblock, boolean dynamic)
	{
		createMultiblock(
				multiblock::getBlock,
				split(unsplitModel, multiblock, false, dynamic),
				split(mirror(unsplitModel, innerModels), multiblock, true, dynamic),
				IEProperties.FACING_HORIZONTAL, IEProperties.MIRRORED
		);
	}

	private void createMultiblock(Supplier<? extends Block> b, ModelFile masterModel)
	{
		createMultiblock(b, masterModel, null, IEProperties.FACING_HORIZONTAL, null);
	}

	private void createMultiblock(Supplier<? extends Block> b, ModelFile masterModel, @Nullable ModelFile mirroredModel,
								  @Nullable Property<Boolean> mirroredState)
	{
		createMultiblock(b, masterModel, mirroredModel, IEProperties.FACING_HORIZONTAL, mirroredState);
	}

	private void createMultiblock(Supplier<? extends Block> b, ModelFile masterModel, @Nullable ModelFile mirroredModel,
								  EnumProperty<Direction> facing, @Nullable Property<Boolean> mirroredState)
	{
		unsplitModels.put(b.get(), masterModel);
		Preconditions.checkArgument((mirroredModel==null)==(mirroredState==null));
		VariantBlockStateBuilder builder = getVariantBuilder(b.get());
		boolean[] possibleMirrorStates;
		if(mirroredState!=null)
			possibleMirrorStates = new boolean[]{false, true};
		else
			possibleMirrorStates = new boolean[1];
		for(boolean mirrored : possibleMirrorStates)
			for(Direction dir : facing.getPossibleValues())
			{
				final int angleY;
				final int angleX;
				if(facing.getPossibleValues().contains(Direction.UP))
				{
					angleX = -90*dir.getStepY();
					if(dir.getAxis()!=Axis.Y)
						angleY = getAngle(dir, 180);
					else
						angleY = 0;
				}
				else
				{
					angleY = getAngle(dir, 180);
					angleX = 0;
				}
				ModelFile model = mirrored?mirroredModel: masterModel;
				PartialBlockstate partialState = builder.partialState()
						.with(facing, dir);
				if(mirroredState!=null)
					partialState = partialState.with(mirroredState, mirrored);
				partialState.setModels(new ConfiguredModel(model, angleX, angleY, true));
			}
	}

	private ModelFile split(NongeneratedModel loc, TemplateMultiblock mb)
	{
		return split(loc, mb, false);
	}

	private ModelFile split(NongeneratedModel loc, TemplateMultiblock mb, boolean mirror)
	{
		return split(loc, mb, mirror, false);
	}

	private ModelFile split(NongeneratedModel loc, TemplateMultiblock mb, boolean mirror, boolean dynamic)
	{
		UnaryOperator<BlockPos> transform = UnaryOperator.identity();
		if(mirror)
		{
			Vec3i size = mb.getSize(null);
			transform = p -> new BlockPos(size.getX()-p.getX()-1, p.getY(), p.getZ());
		}
		return split(loc, mb, transform, dynamic);
	}

	private ModelFile split(
			NongeneratedModel name, TemplateMultiblock multiblock, UnaryOperator<BlockPos> transform, boolean dynamic
	)
	{
		final Vec3i offset = multiblock.getMasterFromOriginOffset();
		Stream<Vec3i> partsStream = multiblock.getStructure(null)
				.stream()
				.filter(info -> !info.state.isAir())
				.map(info -> info.pos)
				.map(transform)
				.map(p -> p.subtract(offset));
		return split(name, partsStream.collect(Collectors.toList()), dynamic);
	}

}
