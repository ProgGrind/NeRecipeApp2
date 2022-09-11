package ru.netology.nerecipeapp.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import androidx.appcompat.widget.SearchView
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipeapp.adapter.RecipesAdapter
import ru.netology.nerecipeapp.data.Category
import ru.netology.nerecipeapp.data.Recipe
import ru.netology.nerecipeapp.databinding.MainFragmentBinding
import ru.netology.nerecipeapp.viewModel.RecipeViewModel

class MainFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToRecipeCreationFragmentEvent.observe(this) { recipe ->
            val direction =
                MainFragmentDirections.actionMainFragmentToRecipeCreationFragment(recipe)
            findNavController().navigate(direction)
        }
    }

    override fun onResume() {
        super.onResume()

        setFragmentResultListener(
            requestKey = RecipeCreationFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != RecipeCreationFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                RecipeCreationFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newRecipe)
        }


        setFragmentResultListener(
            requestKey = RecipeFilterFragment.CHECKBOX_KEY
        ) { requestKey, bundle ->
            if (requestKey != RecipeFilterFragment.CHECKBOX_KEY) return@setFragmentResultListener
            val categories = bundle.getParcelableArrayList<Category>(
                RecipeFilterFragment.CHECKBOX_KEY
            ) ?: return@setFragmentResultListener
            viewModel.showRecipesByCategories(categories)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = MainFragmentBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddButtonClicked()
        }

        if (viewModel.setCategoryFilter) {
            binding.back.isVisible = viewModel.setCategoryFilter
            binding.fab.visibility = View.GONE
            binding.back.setOnClickListener {
                viewModel.showRecipesByCategories(Category.values().toList())
                viewModel.setCategoryFilter = false
                binding.back.visibility = View.GONE
                binding.fab.visibility = View.VISIBLE
                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    adapter.submitList(recipes)
                }
            }
        } else {
            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        adapter.submitList(viewModel.data.value)
                        return true
                    }
                    var recipeList = adapter.currentList
                    recipeList = recipeList.filter { recipe ->
                        recipe.name.lowercase().contains(newText.lowercase())
                    }
                    viewModel.searchRecipe(newText)

                    if (recipeList.isEmpty()) {
                        Toast.makeText(context, "Ничего нет", Toast.LENGTH_SHORT).show()
                        adapter.submitList(recipeList)
                    } else {
                        adapter.submitList(recipeList)
                    }
                    return true
                }
            })
        }


        viewModel.recipeViewEvent.observe(viewLifecycleOwner) { recipeCardId ->
            binding.search.setQuery("", false)
            val direction =
                MainFragmentDirections.actionMainFragmentToRecipeViewFragment(recipeCardId)
            findNavController().navigate(direction)
        }

    }.root

}