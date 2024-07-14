package chen.example.lee.jppractice.ui.vocabularyexamsetting

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import chen.example.lee.jppractice.db.model.Vocabulary
import chen.example.lee.jppractice.other.addBackButton
import chen.example.lee.jppractice.ui.vocabularyexamsetting.model.VocabularyExamSettingEvent
import chen.example.lee.jppractice.ui.vocabularyexamsetting.model.VocabularyExamSettingState
import chen.example.lee.jppractice.ui.vocabularyexamsetting.model.VocabularyExamSettingViewModel
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.FragmentVocabularyexamsettingBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VocabularyExamSettingFragment : Fragment(R.layout.fragment_vocabularyexamsetting) {
    private lateinit var binding : FragmentVocabularyexamsettingBinding
    @Inject lateinit var viewModel : VocabularyExamSettingViewModel
    private var checkBoxs  = arrayListOf<CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addBackButton()
        binding = FragmentVocabularyexamsettingBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initCheckBoxs()
            vocabularyExamSettingButton.setOnClickListener {
                val temp = arrayListOf<String>()
                checkBoxs.forEach {
                    if (it.isChecked) {
                        val radioDisplayText =
                            requireActivity().findViewById<CheckBox>(it.id).text.toString()
                        temp.add(radioDisplayText)

                    }
                }
                viewModel.onEvent(VocabularyExamSettingEvent.GetDataToExam(temp))



            }

            lifecycleScope.launch {
                viewModel.state.collectLatest {
                    if (it.vocabularyExamData.isNotEmpty()){
                        toExam(it.vocabularyExamData)
                    }
                }
            }

        }
        lifecycleScope.launchWhenStarted {
            initGoogleAd()
        }

    }

    private fun toExam(data : List<Vocabulary>){
        viewModel.state.value = VocabularyExamSettingState()
        findNavController().navigate(
            VocabularyExamSettingFragmentDirections.actionVocabularyExamSettingFragmentToVocabularyExamFragment(
                data.toTypedArray()
            )
        )

    }


    private fun FragmentVocabularyexamsettingBinding.initCheckBoxs() {
        checkBoxs.add(douShiCheckBox)
        checkBoxs.add(meishiCheckBox)
        checkBoxs.add(fukushiCheckBox)
        checkBoxs.add(ikeyoshiCheckBox)
        checkBoxs.add(nakeiyoshiCheckBox)

        checkBoxs.forEach {
            if (it == douShiCheckBox){
                it.isChecked = true
            }else{
                it.isChecked = false
            }
        }
    }

    private fun initGoogleAd() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.vocabularyAdView.loadAd(adRequest)
    }
    override fun onPause() {
        binding.vocabularyAdView.pause()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        binding.vocabularyAdView.resume()
    }
    override fun onDestroy() {
        binding.vocabularyAdView.destroy()
        super.onDestroy()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }



}

