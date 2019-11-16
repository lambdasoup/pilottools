package com.lambdasoup.pilottools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.lambdasoup.pilottools.databinding.InstrumentsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class InstrumentsFragment : Fragment() {
    private val vm: InstrumentsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: InstrumentsFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.instruments_fragment,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm

        return binding.root
    }
}
