/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.util.compat.jei.fermenter;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.Arrays;

public class FermenterRecipeCategory extends IERecipeCategory<FermenterRecipe>
{
	public static final RecipeType<FermenterRecipe> TYPE = RecipeType.create(Lib.MODID, "fermenter", FermenterRecipe.class);
	private final IDrawableStatic tankOverlay;

	public FermenterRecipeCategory(IGuiHelper helper)
	{
		super(TYPE, helper, "block.immersiveengineering.fermenter");
		ResourceLocation background = new ResourceLocation(Lib.MODID, "textures/gui/fermenter.png");
		setBackground(helper.createDrawable(background, 6, 12, 126, 59));
		setIcon(new ItemStack(IEBlocks.Multiblocks.FERMENTER.get()));
		tankOverlay = helper.createDrawable(background, 179, 33, 16, 47);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FermenterRecipe recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 2, 7)
				.addItemStacks(Arrays.asList(recipe.input.getMatchingStacks()));
		IRecipeSlotBuilder outputSlotBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 41);
		if(!recipe.itemOutput.get().isEmpty())
			outputSlotBuilder.addItemStack(recipe.itemOutput.get());
		if(recipe.fluidOutput!=null&&!recipe.fluidOutput.isEmpty())
		{
			int tankSize = Math.max(FluidAttributes.BUCKET_VOLUME/4,  recipe.fluidOutput.getAmount());
			builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 9)
					.setFluidRenderer(tankSize, false, 16, 47)
					.setOverlay(tankOverlay, 0, 0)
					.addIngredient(ForgeTypes.FLUID_STACK, recipe.fluidOutput)
					.addTooltipCallback(JEIHelper.fluidTooltipCallback);
		}
	}
}