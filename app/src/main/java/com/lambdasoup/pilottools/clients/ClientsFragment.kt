package com.lambdasoup.pilottools.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lambdasoup.pilottools.R
import com.lambdasoup.pilottools.databinding.ClientsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClientsFragment : Fragment() {
    private val vm: ClientsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ClientsFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.clients_fragment,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm

        return binding.root
    }
}
