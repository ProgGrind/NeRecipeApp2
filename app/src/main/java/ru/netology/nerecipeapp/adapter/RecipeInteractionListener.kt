package ru.netology.nerecipeapp.adapter

import ru.netology.nerecipeapp.data.Recipe

interface RecipeInteractionListener {

    fun onLikeClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onRecipeCardClicked(recipe: Recipe)
    fun onRecipeItemClicked(recipe: Recipe)
}