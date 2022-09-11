package ru.netology.nerecipeapp.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
//import kotlinx.android.synthetic.main.recipe.*
import ru.netology.nerecipeapp.R
import ru.netology.nerecipeapp.adapter.RecipesAdapter
import ru.netology.nerecipeapp.data.Recipe
import ru.netology.nerecipeapp.viewModel.RecipeViewModel
import ru.netology.nerecipeapp.data.*
import ru.netology.nerecipeapp.databinding.FragmentLikeBinding

class LikeFragment : Fragment() {

    private val likeRecipeViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLikeBinding.inflate(layoutInflater, container, false).also { binding ->
        val adapter = RecipesAdapter(likeRecipeViewModel)
        binding.recipesRecycler.adapter = adapter

        likeRecipeViewModel.data.observe(viewLifecycleOwner) { recipes ->

            val likedRecipes = recipes.filter { it.like }
            adapter.submitList(likedRecipes)

            val emptyList = recipes.none { it.like }
            binding.textEmptyList.visibility =
                if (emptyList) View.VISIBLE else View.GONE
            binding.iconEmptyList.visibility =
                if (emptyList) View.VISIBLE else View.GONE
        }

        likeRecipeViewModel.recipeViewEvent.observe(viewLifecycleOwner) {  recipeCardId ->
                        val direction =
                LikeFragmentDirections.actionLikeFragmentToRecipeViewFragment(
                    recipeCardId
                )
            findNavController().navigate(direction)


//            findNavController().navigate(R.id.action_likeFragment_to_recipeViewFragment(it))
        }

        likeRecipeViewModel.navigateToRecipeCreationFragmentEvent.observe(viewLifecycleOwner) { recipe ->
            val direction =
                LikeFragmentDirections.actionLikeFragmentToRecipeCreationFragment(
                    recipe
                )
            findNavController().navigate(direction)
        }
    }.root

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(
            requestKey = RecipeCreationFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != RecipeCreationFragment.REQUEST_KEY) return@setFragmentResultListener
            val newRecipe = bundle.getParcelable<Recipe>(
                RecipeCreationFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            likeRecipeViewModel.onSaveButtonClicked(newRecipe)
        }
    }
}