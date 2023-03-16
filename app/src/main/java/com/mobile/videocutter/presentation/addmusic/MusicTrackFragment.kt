package com.mobile.videocutter.presentation.addmusic

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.MusicTrackFragmentBinding
import com.mobile.videocutter.presentation.model.IViewListener
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE
import handleUiState

class MusicTrackFragment : BaseBindingFragment<MusicTrackFragmentBinding>(R.layout.music_track_fragment) {

    private val musicTrackAdapter = MusicTrackAdapter()
    private val viewModel by viewModels<MusicTrackViewModel>()
    private val listener: IListener? = null

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
        viewModel.getAddMusicTrack()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        lifecycleScope.launchWhenCreated {
            viewModel.musicTrackState.collect {
                handleUiState(it, object : IViewListener {
                    override fun onSuccess() {
                        musicTrackAdapter.submitList(it.data?.map { musicTrack -> MusicTrackAdapter.MusicTrackDisplay(musicTrack) })
                    }
                })
            }
        }
    }

    private fun initRecyclerView() {
        musicTrackAdapter.listener = object : MusicTrackAdapter.OnClickItem {
            override fun onMusicTrackClick(musicTrackDisplay: MusicTrackAdapter.MusicTrackDisplay) {
                listener?.onSave(musicTrackDisplay)
            }
        }

        binding.crvMusicTrack.apply {
            setAdapter(musicTrackAdapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_VERTICAL)
        }
    }

    interface IListener {
        fun onSave(musicTrackDisplay: MusicTrackAdapter.MusicTrackDisplay)
    }
}
