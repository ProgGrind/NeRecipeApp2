package ru.netology.nerecipeapp.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipeapp.R
import ru.netology.nerecipeapp.adapter.showCategories
import ru.netology.nerecipeapp.data.Category
import ru.netology.nerecipeapp.data.Recipe
import ru.netology.nerecipeapp.data.RecipeRepository
import ru.netology.nerecipeapp.databinding.FragmentRecipeCreationBinding
import ru.netology.nerecipeapp.viewModel.RecipeViewModel

class RecipeCreationFragment : Fragment() {

    private val args by navArgs<RecipeCreationFragmentArgs>()

    private val recipeCreationViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecipeCreationBinding.inflate(layoutInflater, container, false).also { binding ->

        val thisRecipe = args.currentRecipe
        if (thisRecipe != null) {
            with(binding) {
                categoryRecipeCheckbox.check(R.id.checkboxAs)
                checkboxEu.text = checkboxEu.context.showCategories(Category.European)
                checkboxAs.text = checkboxAs.context.showCategories(Category.Asian)
                checkboxPa.text = checkboxPa.context.showCategories(Category.Panasian)
                checkboxOr.text = checkboxOr.context.showCategories(Category.Oriental)
                checkboxUs.text = checkboxUs.context.showCategories(Category.American)
                checkboxRu.text = checkboxRu.context.showCategories(Category.Russian)
                checkboxMe.text = checkboxMe.context.showCategories(Category.Mediterranean)
                recipeName.setText(thisRecipe.name)
                recipeEnterField.setText(thisRecipe.recipe)
                txtRecipeDescriptionInput.setText(thisRecipe.description)
                txtTimeInput.setText(thisRecipe.time)
            }
        }

        binding.recipeName.requestFocus()

        binding.categoryRecipeCheckbox.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.checkboxEu -> Category.European.toString()
                R.id.checkboxAs -> Category.Asian.toString()
                R.id.checkboxPa -> Category.Panasian.toString()
                R.id.checkboxOr -> Category.Oriental.toString()
                R.id.checkboxUs -> Category.American.toString()
                R.id.checkboxRu -> Category.Russian.toString()
                R.id.checkboxMe -> Category.Mediterranean.toString()
            }
        }

        binding.btnCreateRecipe.setOnClickListener {
            onOkButtonClicked(binding)
        }
        binding.cancelButton.setOnClickListener {
            onCancelClicked(binding)
        }
    }.root

    private fun onCancelClicked(binding: FragmentRecipeCreationBinding) {
        val currentRecipe = Recipe(
            id = args.currentRecipe?.id ?: RecipeRepository.NEW_RECIPE_ID,
            name = binding.recipeName.text.toString(),
            description = binding.txtRecipeDescriptionInput.text.toString(),
            recipe = binding.recipeEnterField.text.toString(),
            time = binding.txtTimeInput.text.toString(),
            category = getCategory(binding.categoryRecipeCheckbox.checkedRadioButtonId),
            author = "Me"
        )
        recipeCreationViewModel.onCancelClicked(currentRecipe)
        findNavController().navigateUp()
    }

    fun getCategory(check: Int) = when (check) {
        R.id.checkboxEu -> Category.European
        R.id.checkboxAs -> Category.Asian
        R.id.checkboxPa -> Category.Panasian
        R.id.checkboxOr -> Category.Oriental
        R.id.checkboxUs -> Category.American
        R.id.checkboxRu -> Category.Russian
        R.id.checkboxMe -> Category.Mediterranean
//        R.id.checkboxEu -> Category.European.toString()
//        R.id.checkboxAs -> Category.Asian.toString()
//        R.id.checkboxPa -> Category.Panasian.toString()
//        R.id.checkboxOr -> Category.Oriental.toString()
//        R.id.checkboxUs -> Category.American.toString()
//        R.id.checkboxRu -> Category.Russian.toString()
//        R.id.checkboxMe -> Category.Mediterranean.toString()
        else -> throw IllegalArgumentException("Unknown type: $check")
    }

    private fun onOkButtonClicked(binding: FragmentRecipeCreationBinding) {
        val currentRecipe = Recipe(
            id = args.currentRecipe?.id ?: RecipeRepository.NEW_RECIPE_ID,
            name = binding.recipeName.text.toString(),
            description = binding.txtRecipeDescriptionInput.text.toString(),
            recipe = binding.recipeEnterField.text.toString(),
            time = binding.txtTimeInput.text.toString(),
            category = getCategory(binding.categoryRecipeCheckbox.checkedRadioButtonId),
            author = "Me"
        )
        if (emptyFieldsCheck(recipe = currentRecipe)) {
            val resultBundle = Bundle(1)
            resultBundle.putParcelable(RESULT_KEY, currentRecipe)
            setFragmentResult(REQUEST_KEY, resultBundle)
            findNavController().popBackStack()
        }
    }

    private fun emptyFieldsCheck(recipe: Recipe): Boolean {
        return if (recipe.name.isBlank()
            || recipe.recipe.isBlank()
            || recipe.description.isBlank()
            || recipe.time.isBlank()
        ) {
            Toast.makeText(activity, "Заполните все поля", Toast.LENGTH_LONG).show()
            false
        } else true
    }


    companion object {
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "recipeNewContent"
    }
}