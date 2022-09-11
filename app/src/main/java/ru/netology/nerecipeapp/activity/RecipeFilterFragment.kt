package ru.netology.nerecipeapp.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipeapp.adapter.showCategories
import ru.netology.nerecipeapp.data.Category
import ru.netology.nerecipeapp.data.Recipe
import ru.netology.nerecipeapp.databinding.FragmentRecipeFilterBinding
import ru.netology.nerecipeapp.viewModel.RecipeViewModel

class RecipeFilterFragment : Fragment() {


    private val filterViewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecipeFilterBinding.inflate(layoutInflater, container, false).also { binding ->

        with(binding) {
            checkboxEu.text = checkboxEu.context.showCategories(Category.European)
            checkboxAs.text = checkboxAs.context.showCategories(Category.Asian)
            checkboxPa.text = checkboxPa.context.showCategories(Category.Panasian)
            checkboxOr.text = checkboxOr.context.showCategories(Category.Oriental)
            checkboxUs.text = checkboxUs.context.showCategories(Category.American)
            checkboxRu.text = checkboxRu.context.showCategories(Category.Russian)
            checkboxMe.text = checkboxMe.context.showCategories(Category.Mediterranean)

            binding.setFilter.setOnClickListener {
                onOkButtonClicked(binding)
            }
        }
    }.root

    private fun onOkButtonClicked(binding: FragmentRecipeFilterBinding) {
        val categoryList = arrayListOf<Category>()
        var checked = 7
        val nothingIsChecked = 0

        if (binding.checkboxEu.isChecked) {
            categoryList.add(Category.European)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxAs.isChecked) {
            categoryList.add(Category.Asian)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxPa.isChecked) {
            categoryList.add(Category.Panasian)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxOr.isChecked) {
            categoryList.add(Category.Oriental)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxUs.isChecked) {
            categoryList.add(Category.American)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxRu.isChecked) {
            categoryList.add(Category.Russian)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (binding.checkboxMe.isChecked) {
            categoryList.add(Category.Mediterranean)
            filterViewModel.setCategoryFilter = true
        } else {
            checked--
        }
        if (checked == nothingIsChecked) {
            Toast.makeText(activity, "There are no filters set", Toast.LENGTH_LONG).show()
        } else {
            filterViewModel.showRecipesByCategories(categoryList)
            val resultBundle = Bundle(1)
            resultBundle.putParcelableArrayList(CHECKBOX_KEY, categoryList)
            setFragmentResult(CHECKBOX_KEY, resultBundle)
            findNavController().popBackStack()
        }
    }


    companion object {
        const val CHECKBOX_KEY = "checkBoxContent"
    }

}