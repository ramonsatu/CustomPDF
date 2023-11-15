package com.ramonpsatu.custompdf.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ramonpsatu.custompdf.R
import com.ramonpsatu.custompdf.databinding.FragmentCustomDialogBinding

class CustomDialogFragment(private  val intent: Intent, private val condition: Boolean): DialogFragment() {
    companion object {
        const val TAG = "CustomDialogFragment"
    }

    private var _binding: FragmentCustomDialogBinding? = null
    private val binding get() = _binding!!


   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.fragment_custom_dialog)
        builder.create()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        with(binding){

            if (condition){
                textViewInforSignOut.text = getString(R.string.text_dialog_activity_exists)

            }else{
                tbnSignOutNo.visibility = View.INVISIBLE
                textViewInforSignOut.text = getString(R.string.text_dialog_activity_no_exists)
                tbnSignOutYes.text = getString(R.string.text_dialog_activity_understood_button)

            }

        }

        binding.tbnSignOutYes.setOnClickListener {
            if (condition){
                requireActivity().startActivity(intent)
            }
            this.dismiss()
        }

        binding.tbnSignOutNo.setOnClickListener {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}