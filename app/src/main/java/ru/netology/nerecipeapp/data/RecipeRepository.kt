package ru.netology.nerecipeapp.data

import androidx.lifecycle.LiveData

interface RecipeRepository {

    val data: LiveData<List<Recipe>>

    fun like(recipeId: Long)
    fun delete(recipeId: Long)
    fun save(recipe: Recipe)
    fun search(recipeName: String)
    fun getCategory(category: Category)
    fun update()

    companion object {
        const val NEW_RECIPE_ID = 0L
    }
}