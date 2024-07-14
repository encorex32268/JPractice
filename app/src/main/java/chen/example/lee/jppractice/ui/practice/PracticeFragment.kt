package chen.example.lee.jppractice.ui.practice


import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chen.example.lee.jppractice.db.model.*
import chen.example.lee.jppractice.other.addBackButton
import chen.example.lee.jppractice.other.getResourcesString
import chen.example.lee.jppractice.other.getVoiceIconByStatus
import chen.example.lee.jppractice.repositories.SharedPreferencesRepository
import chen.example.lee.jppractice.ui.practice.model.PracticeEvent
import chen.example.lee.jppractice.ui.viewmodels.PracticeFragmentViewModel
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.PracticeAlertviewBinding
import com.example.lee.jppractice.databinding.FragmentPracticeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PracticeFragment : Fragment(R.layout.fragment_practice) , View.OnClickListener {
    private lateinit var userAnswerString: String
    private lateinit var userJPLetter : JPLetter
    private lateinit var ansLetter: JPLetter

    private lateinit var buttonResources :ArrayList<Button>
    private lateinit var binding:FragmentPracticeBinding
    private var isOpenVoice = false

    private lateinit var nowQuestion : PracticeQuestion

    private val viewModel  : PracticeFragmentViewModel by viewModels()
    private val args: PracticeFragmentArgs by navArgs()

    @Inject lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    @Inject lateinit var soundHandler : SoundHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isOpenVoice = sharedPreferencesRepository.getPracticeVoiceBoolean()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addBackButton()
        binding = FragmentPracticeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViewById()
        viewModel.onEvent(PracticeEvent.CreatePracticeData(args.practiceType))
        viewModel.onEvent(PracticeEvent.NextQuestion)
        lifecycleScope.launch {
            viewModel.state.collect {
                binding.apply {
                    goodjobCount.text = it.correct.toString()
                    notGoodCount.text = it.wrong.toString()
                    nowQuestion = it.practiceQuestion
                    val questions = nowQuestion.questions
                    for(index in 0..3){
                        buttonResources[index].text = questions[index].roma
                    }
                    ansLetter = questions.random()
                    binding.showTextViewPractice.text = "["+ansLetter.word +"]"

                }
            }


        }
    }
    private fun findViewById() {
        buttonResources = arrayListOf(
            binding.ansOne,
            binding.ansTwo,
            binding.ansThree,
            binding.ansFour
        )
        buttonResources.forEach {
            it.setOnClickListener(this)
        }
    }
    override fun onClick(v: View) {
        handleAnswer(v.id)
    }

    private fun handleAnswer(id: Int) {
        userAnswerString = requireActivity().findViewById<Button>(id)?.text.toString()
        viewModel.state.value.practiceQuestion.questions.forEach {
            if (it.roma == userAnswerString){
                userJPLetter = it
            }
        }
        val answerRoma = ansLetter.roma
        if (userAnswerString == answerRoma) {
            viewModel.onEvent(PracticeEvent.CorrectPlus)
            if (isOpenVoice) {
                soundHandler.sound(
                    viewModel.state.value.mPracticeType.saveWord,
                    viewModel.state.value.mPracticeType.saveToneInt,
                    answerRoma)
            }
        } else {
            AlertDialog.Builder(requireContext())
                    .setView(getCustomAlertView(userJPLetter))
                    .setTitle(getResourcesString(R.string.practice_alert_title)?:"")
                    .setPositiveButton(getResourcesString(R.string.practice_alert_ok)?:""
                    ) { _, _ -> }
                    .setOnCancelListener {}
                    .setOnDismissListener{
                    viewModel.onEvent(PracticeEvent.WrongPlus)
                }
                .show()
        }
        lifecycleScope.launch {
            viewModel.onEvent(PracticeEvent.RemoveQuestionFromQuestionList(nowQuestion))
            viewModel.onEvent(PracticeEvent.NextQuestion)
        }
    }
    private fun getCustomAlertView(userJPLetter: JPLetter): View {
        val alertBinding = PracticeAlertviewBinding.inflate(layoutInflater)
        alertBinding.apply {
            alertYourRoma.text = userJPLetter.roma
            alertYourWord.text = "[${userJPLetter.word}]"
            alertCurrentRoma.text = ansLetter.roma
            alertCurrentWord.text = "[${ansLetter.word}]"
        }
        return alertBinding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.practice_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.practiceSpeaker)?.apply {
            isChecked = isOpenVoice
            icon = getVoiceIconByStatus(isOpenVoice)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                findNavController().popBackStack()
            }
            R.id.practiceSpeaker->{
                isOpenVoice = !isOpenVoice
                sharedPreferencesRepository.setPracticeVoiceBoolean(isOpenVoice)
                item.icon = getVoiceIconByStatus(isOpenVoice)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

