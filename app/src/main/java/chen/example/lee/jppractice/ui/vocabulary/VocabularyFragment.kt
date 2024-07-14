package chen.example.lee.jppractice.ui.vocabulary

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chen.example.lee.jppractice.db.model.JPContract
import chen.example.lee.jppractice.db.model.SoundHandler
import chen.example.lee.jppractice.other.getResourcesStringArray
import chen.example.lee.jppractice.other.getVoiceIconByStatus
import chen.example.lee.jppractice.other.removeBackButton
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import chen.example.lee.jppractice.ui.vocabulary.model.VocabularyEvent
import chen.example.lee.jppractice.ui.vocabulary.model.VocabularyFragmentViewModel
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.FragmentVocabularyBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyFragment : Fragment(R.layout.fragment_vocabulary) , TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
    private lateinit var vocabularyAdapter : VocabularyAdapter
    private var isOpenVoice = false

    private lateinit var binding : FragmentVocabularyBinding
    @Inject lateinit var viewModel : VocabularyFragmentViewModel
    private var types = arrayOf<String>()
    @Inject lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    @Inject lateinit var soundHandler: SoundHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOpenVoice = sharedPreferencesRepository.getVocabularyVoiceBoolean()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        removeBackButton()
        binding = FragmentVocabularyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        binding.apply {
            types = getResourcesStringArray(R.array.vocabulary_types)
            vocabularyTabLayout.apply {
                types.forEach {
                    addTab(this.newTab().setText(it))
                }
                addOnTabSelectedListener(this@VocabularyFragment)

            }
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            vocabularyRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                vocabularyAdapter = VocabularyAdapter(
                    arrayListOf(),
                    soundHandler,
                    sharedPreferencesRepository
                )
                adapter = vocabularyAdapter
            }
            lifecycleScope.launch{
                viewModel.onEvent(VocabularyEvent.TypeChanged("動詞"))
            }

            lifecycleScope.launch {
                viewModel.state.collectLatest {
                    Log.d("TAG", "setRecyclerView: ")
                    binding.vocabularyRecycler.apply {
                        layoutManager = LinearLayoutManager(context)
                        vocabularyAdapter.apply {
                            vocabularys = it.vocabularyList
                        }
                        adapter = vocabularyAdapter
                    }

                }
            }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.vocabulary_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.voice_switch_vocabulary)?.apply {
            isChecked = isOpenVoice
            icon = getVoiceIconByStatus(isOpenVoice)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.voice_switch_vocabulary->{
                isOpenVoice = !isOpenVoice
                sharedPreferencesRepository.setVocabularyVoiceBoolean(isOpenVoice)
                item.icon = getVoiceIconByStatus(isOpenVoice)
            }
            R.id.exam_vocabulary->{
                val action = VocabularyFragmentDirections.actionVocabularyFragmentToVocabularyExamSettingFragment()
                findNavController().navigate(action)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTabReselected(p0: TabLayout.Tab) {}
    override fun onTabUnselected(p0: TabLayout.Tab) {}
    override fun onTabSelected(tab: TabLayout.Tab) {
        viewModel.onEvent(VocabularyEvent.TypeChanged(JPContract.getTypeChinese(tab.text.toString())))

    }


}
