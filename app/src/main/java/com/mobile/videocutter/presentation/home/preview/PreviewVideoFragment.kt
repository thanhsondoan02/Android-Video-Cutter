package com.mobile.videocutter.presentation.home.preview

import com.mobile.videocutter.R
import com.mobile.videocutter.base.common.binding.BaseBindingFragment
import com.mobile.videocutter.databinding.PreviewVideoFragmentBinding

class PreviewVideoFragment : BaseBindingFragment<PreviewVideoFragmentBinding>(R.layout.preview_video_fragment) {
    companion object {
        const val VIDEO_PATH = "VIDEO_PATH"
        const val VIDEO_DURATION = "VIDEO_DURATION"
    }

    override fun onInitView() {
        super.onInitView()
//        initOnClick()
//        initPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.pvPreviewVideo.player?.release()
    }

    override fun onPause() {
        super.onPause()
//        binding.pvPreviewVideo.player?.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
//        binding.pvPreviewVideo.player?.playWhenReady = true
    }

//    private fun initOnClick() {
//        binding.pvPreviewVideo.setOnSafeClick {
//            if (getCurrentFragment() is VideoControllerFragment) {
//                backFragment()
//            } else {
//                addFragment(
//                    VideoControllerFragment().apply { player = binding.pvPreviewVideo.player },
//                    bundleOf(VideoControllerFragment.VIDEO_DURATION to requireArguments().getLong(VIDEO_DURATION))
//                )
//            }
//        }
//    }

//    private fun initPlayer() {
//        binding.pvPreviewVideo.player = ExoPlayer.Builder(requireContext()).build().apply {
//            requireArguments().getString(VIDEO_PATH)?.let {
//                setMediaItem(MediaItem.fromUri(it))
//            }
//            prepare()
//            playWhenReady = true
//
//            // restart video when it ends
//            addListener(object : Player.Listener {
//                override fun onEvents(player: Player, events: Player.Events) {
//                    super.onEvents(player, events)
//                    if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
//                        if (player.playbackState == Player.STATE_ENDED) {
//                            binding.pvPreviewVideo.player?.seekTo(0)
//                            binding.pvPreviewVideo.player?.playWhenReady = true
//                        }
//                    }
//                }
//            })
//        }
//    }
}
