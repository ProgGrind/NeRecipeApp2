package ru.netology.nerecipeapp.db

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nerecipeapp.data.Category
import ru.netology.nerecipeapp.data.Recipe
import ru.netology.nerecipeapp.data.RecipeRepository
import kotlin.properties.Delegates

class SharedPrefsRecipeRepository(
    application: Application
) : RecipeRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )
    override val data: MutableLiveData<List<Recipe>>

    init {
        val serializedRecipes = prefs.getString(RECIPES_PREFS_KEY, null)
        val posts: List<Recipe> = if (serializedRecipes != null) {
            Json.decodeFromString(serializedRecipes)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit { putLong(NEXT_ID_PREFS_KEY, newValue) }
    }

    private var recipes
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedRecipes = Json.encodeToString(value)
                putString(RECIPES_PREFS_KEY, serializedRecipes)
            }
            data.value = value
        }

    override fun like(recipeId: Long) {
        recipes =
            recipes.map {
                if (it.id == recipeId)
                    it.copy(like = !it.like)
                else it
            }
        data.value = recipes
    }

    override fun delete(recipeId: Long) {
        recipes =
            recipes.filter { it.id != recipeId }
        data.value = recipes
    }

    override fun save(recipe: Recipe) {
        if (recipe.id == RecipeRepository.NEW_RECIPE_ID) insert(recipe) else update(recipe)
    }

    private fun update(recipe: Recipe) {
        recipes = recipes.map {
            if (it.id == recipe.id) recipe else it
        }
        data.value = recipes
    }

    private fun insert(recipe: Recipe) {
        recipes =
            listOf(
                recipe.copy(
                    id = nextId++
                )
            ) + recipes
        data.value = recipes
    }

    override fun search(recipeName: String) {
        recipes.filter {
            it.name == recipeName
        }
        data.value = recipes
    }


    override fun getCategory(category: Category) {
        recipes.find {
            it.category == category
        }
        data.value = recipes
    }

    override fun update() {
        data.value = recipes
    }

    private companion object {
        const val RECIPES_PREFS_KEY = "recipes"
        const val NEXT_ID_PREFS_KEY = "nextId"
    }
}