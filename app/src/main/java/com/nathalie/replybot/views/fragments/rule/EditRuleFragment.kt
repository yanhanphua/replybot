package com.nathalie.replybot.views.fragments.rule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.nathalie.replybot.R
import com.nathalie.replybot.viewModel.BaseViewModel
import com.nathalie.replybot.viewModel.rule.EditRuleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditRuleFragment : BaseRuleFragment() {
    override val viewModel: EditRuleViewModel by viewModels()

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

        val navArgs: EditRuleFragmentArgs by navArgs()
        viewModel.getRuleById(navArgs.id)

        viewModel.rule.observe(viewLifecycleOwner) { rule ->
            binding?.run {
                tvTitle.text = "Edit Rule"
                btnDelete.isVisible = true
                etKeyword.setText(rule.keyword)
                etMsg.setText(rule.msg)
                checkWhatsapp.isChecked = rule.whatsapp
                checkFacebook.isChecked = rule.facebook

                btnSave.setOnClickListener { _ ->
                    val updatedRule = getRule()?.copy(userId = rule.userId)
                    updatedRule?.let {
                        viewModel.editRule(navArgs.id, updatedRule)
                    }
                }

                btnDelete.setOnClickListener {
                    viewModel.deleteRule(navArgs.id)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                popBackWithToast("finish_edit_rule", "Rule updated successfully!")
            }
        }

        lifecycleScope.launch {
            viewModel.finishDelete.collect {
                popBackWithToast("finish_delete_rule", "Rule deleted successfully!")
            }
        }
    }

    private fun popBackWithToast(requestKey: String, toastMsg: String) {
        val bundle = Bundle()
        bundle.putBoolean("refresh", true)
        setFragmentResult(requestKey, bundle)
        navController.popBackStack()
        Toast.makeText(requireContext(), toastMsg, Toast.LENGTH_LONG)
            .show()
    }
}