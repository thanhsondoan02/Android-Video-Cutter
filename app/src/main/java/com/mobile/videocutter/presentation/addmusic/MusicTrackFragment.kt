package com.mobile.videocutter.presentation.addmusic

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.MusicTrackFragmentBinding
import com.mobile.videocutter.domain.model.mockMusicTrackList
import com.mobile.videocutter.presentation.widget.recyclerview.LAYOUT_MANAGER_MODE

class MusicTrackFragment : BaseBindingFragment<MusicTrackFragmentBinding>(R.layout.music_track_fragment) {

    private val musicTrackAdapter = MusicTrackAdapter()
//    private val viewModel by viewModels<MusicTrackViewModel>()

    override fun onInitView() {
        super.onInitView()
        initRecyclerView()
//        viewModel.getAddMusicTrack()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

//        lifecycleScope.launchWhenCreated {
//            viewModel.musicTrackState.collect {
//                handleUiState(it, object : IViewListener {
//                    override fun onSuccess() {
//                        musicTrackAdapter.submitList(it.data?.map { musicTrack -> MusicTrackAdapter.MusicTrackDisplay(musicTrack) })
//                    }
//                })
//            }
//        }
    }

    private fun initRecyclerView() {
//        musicTrackAdapter.listener = object : MusicTrackAdapter.IListener {
//            override fun onMusicTrackClick(musicTrack: MusicTrack?, state: MusicTrackAdapter.STATE_MUSIC_TRACK, size: Int) {
//
//            }
//        }

        binding.crvMusicTrack.apply {
            setAdapter(musicTrackAdapter)
            setLayoutManagerMode(LAYOUT_MANAGER_MODE.LINEAR_VERTICAL)
        }

        musicTrackAdapter.submitList(mockMusicTrackList(50).map { MusicTrackAdapter.MusicTrackDisplay(it) })

    }
}
