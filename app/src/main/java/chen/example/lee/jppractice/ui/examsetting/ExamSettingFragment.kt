package chen.example.lee.jppractice.ui.examsetting


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import chen.example.lee.jppractice.other.getResourcesString
import chen.example.lee.jppractice.other.getResourcesStringWithVar
import chen.example.lee.jppractice.db.model.JPContract.COUNT_PICK
import chen.example.lee.jppractice.db.model.JPContract.MIN_VALUE
import chen.example.lee.jppractice.db.model.JPContract.TONE_BEND_PICK
import chen.example.lee.jppractice.db.model.JPContract.TONE_FOOTY_PICK
import chen.example.lee.jppractice.db.model.JPContract.WORD_ALL_PICK
import chen.example.lee.jppractice.db.model.PracticeType
import chen.example.lee.jppractice.other.removeBackButton
import chen.example.lee.jppractice.utils.ToneWordValueUtils
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.FragmentExamsettingBinding
import com.google.android.gms.ads.*
import com.shawnlin.numberpicker.NumberPicker


class ExamSettingFragment : Fragment(R.layout.fragment_examsetting), View.OnClickListener,
    NumberPicker.OnValueChangeListener {
    private lateinit var binding: FragmentExamsettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        removeBackButton()
        binding = FragmentExamsettingBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        lifecycleScope.launchWhenStarted {
            initGoogleAd()
        }
    }
    private fun init() {
        binding.practicetriggerButton.setOnClickListener(this)
        initPicker()

    }
    private fun initPicker() {
            binding.wordPicker.apply {
                val wordArray =resources.getStringArray(R.array.examSetting_word_picker)
                minValue = MIN_VALUE
                maxValue = wordArray.size
                displayedValues = wordArray
                setOnValueChangedListener(this@ExamSettingFragment)
            }
            binding.tonePicker.apply {
                val toneArray =resources.getStringArray(R.array.examSetting_tone_picker)
                minValue = MIN_VALUE
                maxValue = toneArray.size
                displayedValues = toneArray
                setOnValueChangedListener(this@ExamSettingFragment)
            }
            binding.countPicker.apply {
                val countArray = getCountArray()
                minValue = MIN_VALUE
                maxValue = countArray.size
                displayedValues = countArray.toTypedArray()
            }

    }

    private fun getCountArray(): ArrayList<String> {
        val countArrayList = arrayListOf<String>()
        for (i in 0 until COUNT_PICK) {
            when {
                i == 0 -> {
                    countArrayList.add(getResourcesString(R.string.examSetting_count_all)?:"")
                }
                i < 10 -> {
                    countArrayList.add(getResourcesStringWithVar(R.string.examSetting_count_other,i))
                }
                i == 10 ->{
                    countArrayList.add(getResourcesStringWithVar(R.string.examSetting_count_other_singlemode,i/10))
                }
                else -> {
                    countArrayList.add(getResourcesStringWithVar(R.string.examSetting_count_other_singlemode,i%10+1))
                }
            }
        }
        return countArrayList
    }

    override fun onClick(view: View) {
        binding.apply {
            val practiceType = PracticeType(
                ToneWordValueUtils.wordValueCheck(wordPicker.value),
                ToneWordValueUtils.toneValueCheck(tonePicker.value),
                countPicker.value-1
            )
            val action = ExamSettingFragmentDirections.actionExamSettingFragmentToPracticeFragment(practiceType)
            findNavController().navigate(action)
        }



    }

    private fun initGoogleAd() {
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }
    override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }
    override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }

        override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        binding.apply {
        when(picker?.id) {
            R.id.wordPicker-> {
                if (newVal == WORD_ALL_PICK){
                    tonePicker.visibility = View.INVISIBLE
                    countPicker.visibility = View.INVISIBLE
                }else{
                    tonePicker.visibility = View.VISIBLE
                    if (tonePicker.value == TONE_FOOTY_PICK ||
                        tonePicker.value == TONE_BEND_PICK
                    ){
                        countPicker.visibility = View.INVISIBLE
                    }else{
                        countPicker.visibility = View.VISIBLE
                    }
                }
            }
            R.id.tonePicker->{
                if (newVal == TONE_FOOTY_PICK || newVal ==TONE_BEND_PICK){
                    countPicker.visibility = View.INVISIBLE
                }else{
                    countPicker.visibility = View.VISIBLE
                }
            }

        }
        }
    }

}




