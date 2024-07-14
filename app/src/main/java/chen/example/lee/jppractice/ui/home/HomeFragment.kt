package chen.example.lee.jppractice.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import chen.example.lee.jppractice.db.model.AllWordToneData
import chen.example.lee.jppractice.db.model.JPVoice
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.other.getResourcesString
import chen.example.lee.jppractice.other.getVoiceIconByStatus
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import chen.example.lee.jppractice.ui.home.model.HomeEvent
import chen.example.lee.jppractice.ui.home.model.HomeFragmentViewModel
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

    private lateinit var homeRecyclerAdapter: HomeRecyclerAdapter
    private var isReverse = false
    private var isOpenVoice = false

    private lateinit var binding: FragmentHomeBinding
    @Inject lateinit var viewModel : HomeFragmentViewModel
    @Inject lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    @Inject lateinit var soundHandler: SoundHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOpenVoice = sharedPreferencesRepository.getHomeVoiceBoolean()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.apply {
            homeTabLayout.apply {
                addTab(this.newTab().setText(getResourcesString(R.string.home_alltone_clear)))
                addTab(this.newTab().setText(getResourcesString(R.string.home_alltone_foodty)))
                addTab(this.newTab().setText(getResourcesString(R.string.home_alltone_bend)))
                addOnTabSelectedListener(this@HomeFragment)
            }

            recyclerAlltone.apply{
                setHasFixedSize(true)
                homeRecyclerAdapter = HomeRecyclerAdapter(AllWordToneData(context),soundHandler,sharedPreferencesRepository)
                adapter = homeRecyclerAdapter

            }
            lifecycleScope.launch {
                viewModel.state.collectLatest {
                    recyclerAlltone.layoutManager = GridLayoutManager(requireActivity(),it.gridlayoutCount)
                    homeRecyclerAdapter.apply {
                        allWordToneData =  it.allWordToneData
                        setAllReverse(it.isAllReverse)
                    }
                }
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.alltone_menu, menu)
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.voice_switch)?.apply {
            isChecked = isOpenVoice
            icon = getVoiceIconByStatus(isOpenVoice)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.word_change ->{
                isReverse  = !isReverse
                viewModel.onEvent(HomeEvent.ToReverse(isReverse))
            }
            R.id.voice_switch->{
                isOpenVoice = !isOpenVoice
                sharedPreferencesRepository.setHomeVoiceBoolean(isOpenVoice)
                item.icon = getVoiceIconByStatus(isOpenVoice)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onTabUnselected(p0: TabLayout.Tab) {}
    override fun onTabReselected(p0: TabLayout.Tab) {}
    override fun onTabSelected(p0: TabLayout.Tab) {
        when(p0.text){
            getResourcesString(R.string.home_alltone_clear)->{
               viewModel.onEvent(HomeEvent.JPVoiceChanged(jpVoice = JPVoice.Clear))
             }
            getResourcesString(R.string.home_alltone_foodty)->{
                viewModel.onEvent(HomeEvent.JPVoiceChanged(jpVoice = JPVoice.Foodty))
            }
            getResourcesString(R.string.home_alltone_bend)->{
                viewModel.onEvent(HomeEvent.JPVoiceChanged(jpVoice = JPVoice.Bend))
            }

        }
        viewModel.onEvent(HomeEvent.ToReverse(isReverse))



    }

}
