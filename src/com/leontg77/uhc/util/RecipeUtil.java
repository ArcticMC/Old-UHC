package com.leontg77.uhc.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeUtil {
	public static final short DATA_WILDCARD = 32767;

	/**
	 * Check if two recipes are equal
	 * @param recipe The 1st recipe
	 * @param recipe1 The 2nd recipe
	 * @return True if they are equal
	 */
	public static boolean areEqual(Recipe recipe, Recipe recipe1) {
		return recipe == recipe1 || recipe != null && recipe1 != null && recipe.getResult().equals(recipe1.getResult()) && match(recipe, recipe1);
	}

	/**
	 * Check if two recipes are similar
	 * @param recipe The 1st recipe
	 * @param recipe1 The 2nd recipe
	 * @return True if they are similar
	 */
	public static boolean areSimilar(Recipe recipe, Recipe recipe1) {
		return recipe == recipe1 || recipe != null && recipe1 != null && match(recipe, recipe1);
	}

	/**
	 * Matches two recipes
	 * @param recipe The 1st recipe
	 * @param recipe1 The 2nd recipe
	 * @return True if they match
	 */
	private static boolean match(Recipe recipe, Recipe recipe1) {
		if (recipe instanceof ShapedRecipe) {
			if (!(recipe1 instanceof ShapedRecipe))
				return false;
			ShapedRecipe shapedrecipe = (ShapedRecipe) recipe;
			ShapedRecipe shapedrecipe1 = (ShapedRecipe) recipe1;
			ItemStack aitemstack[] = shapeToMatrix(shapedrecipe.getShape(), shapedrecipe.getIngredientMap());
			ItemStack aitemstack1[] = shapeToMatrix(shapedrecipe1.getShape(), shapedrecipe1.getIngredientMap());
			if (!Arrays.equals(aitemstack, aitemstack1)) {
				mirrorMatrix(aitemstack);
				return Arrays.equals(aitemstack, aitemstack1);
			} else {
				return true;
			}
		}
		if (recipe instanceof ShapelessRecipe) {
			if (!(recipe1 instanceof ShapelessRecipe))
				return false;
			ShapelessRecipe shapelessrecipe = (ShapelessRecipe) recipe;
			ShapelessRecipe shapelessrecipe1 = (ShapelessRecipe) recipe1;
			List<ItemStack> list = shapelessrecipe.getIngredientList();
			List<ItemStack> list1 = shapelessrecipe1.getIngredientList();
			if (list.size() != list1.size())
				return false;
			for (Iterator<ItemStack> iterator = list1.iterator(); iterator.hasNext();) {
				ItemStack itemstack = (ItemStack) iterator.next();
				if (!list.remove(itemstack)) {
					return false;
				}
			}

			return list.isEmpty();
		} else if (recipe instanceof FurnaceRecipe) {
			if (!(recipe1 instanceof FurnaceRecipe)) {
				return false;
			} else {
				FurnaceRecipe furnacerecipe = (FurnaceRecipe) recipe;
				FurnaceRecipe furnacerecipe1 = (FurnaceRecipe) recipe1;
				return furnacerecipe.getInput().getType() == furnacerecipe1.getInput().getType();
			}
		} else {
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported recipe type: '").append(recipe).append("', update RecipeUtil.java!").toString());
		}
	}

	@SuppressWarnings("rawtypes")
	private static ItemStack[] shapeToMatrix(String as[], Map map) {
		ItemStack aitemstack[] = new ItemStack[9];
		int i = 0;
		for (int j = 0; j < as.length; j++) {
			char ac[] = as[j].toCharArray();
			int k = ac.length;
			for (int l = 0; l < k; l++) {
				char c = ac[l];
				aitemstack[i] = (ItemStack) map.get(Character.valueOf(c));
				i++;
			}

			i = (j + 1) * 3;
		}

		return aitemstack;
	}

	private static void mirrorMatrix(ItemStack aitemstack[]) {
		for (int i = 0; i < 3; i++) {
			ItemStack itemstack = aitemstack[i * 3];
			aitemstack[i * 3] = aitemstack[i * 3 + 2];
			aitemstack[i * 3 + 2] = itemstack;
		}
	}
}