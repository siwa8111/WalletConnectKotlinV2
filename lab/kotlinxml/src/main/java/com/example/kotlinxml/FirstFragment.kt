package com.example.kotlinxml

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kotlinxml.databinding.FragmentFirstBinding
import com.walletconnect.web3.modal.domain.configuration.Config
import com.walletconnect.web3.modal.ui.navigateToWeb3Modal

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val config = Config.Connect(
            uri = "wc:7f6e504bfad60b485450578e05678ed3e8e8c4751d3c6160be17160d63ec90f9@2",
            chains = listOf("eip155")
        )

        binding.buttonFirst.setOnClickListener { findNavController().navigateToWeb3Modal(config) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}