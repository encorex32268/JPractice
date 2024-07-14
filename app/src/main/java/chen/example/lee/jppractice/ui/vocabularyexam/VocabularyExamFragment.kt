package chen.example.lee.jppractice.ui.vocabularyexam

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import chen.example.lee.jppractice.db.model.JPContract.FUKUSHI
import chen.example.lee.jppractice.db.model.JPContract.IKEIYOUSHI
import chen.example.lee.jppractice.db.model.JPContract.NAKEIYOUSHI
import chen.example.lee.jppractice.db.model.Vocabulary
import chen.example.lee.jppractice.other.addBackButton
import chen.example.lee.jppractice.other.getResourcesString
import chen.example.lee.jppractice.ui.vocabularyexam.model.VocabularyExamEvent
import com.example.lee.jppractice.R
import com.example.lee.jppractice.databinding.FragmentVocabularyexamBinding
import com.example.lee.jppractice.databinding.PracticeAlertviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VocabularyExamFragment : Fragment(R.layout.fragment_vocabularyexam), View.OnClickListener {
    private lateinit var binding :FragmentVocabularyexamBinding

    private val args: VocabularyExamFragmentArgs by navArgs()
    private var examVocabularys = mutableListOf<Vocabulary>()

    private val viewModel : VocabularyExamViewModel by viewModels()
    private lateinit var questionVocabulary : Vocabulary

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addBackButton()
        binding = FragmentVocabularyexamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        examVocabularys.addAll(args.vocabularyList)
        init()
    }
    private fun init() {

        viewModel.onEvent(VocabularyExamEvent.NextQuestion(examVocabularys.random()))

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.apply {
                    vocabularyExamGoodjobCount.text = it.correct.toString()
                    vocabularyExamNotgoodCount.text = it.wrong.toString()
                    questionVocabulary = it.question!!
                        examQuestionTextView.text =
                            if (questionVocabulary.word.isEmpty()) {
                                "(${it.questionCounter}) \n ${questionVocabulary.wordExplain}"
                            } else {
                                "(${it.questionCounter}) \n ${questionVocabulary.word}"
                            }
                        examQuestionExampleTextView.text =
                            if (questionVocabulary.type == FUKUSHI ||
                                questionVocabulary.type == IKEIYOUSHI||
                                questionVocabulary.type == NAKEIYOUSHI
                            ) "" else questionVocabulary.example
                        val randomList = arrayListOf(
                            questionVocabulary.hiragana, questionVocabulary.wrongWord
                        )
                        randomList.shuffle()
                        examChoiceBtn.apply {
                            text = randomList[0]
                            setOnClickListener(this@VocabularyExamFragment)
                        }
                        examChoiceBtn2.apply {
                            text = randomList[1]
                            setOnClickListener(this@VocabularyExamFragment)
                        }

                }
            }

        }

    }


    override fun onClick(view: View?) {

        val userChoiceAnswer = view?.id?.let { requireActivity().findViewById<Button>(it).text }
        viewModel.onEvent(VocabularyExamEvent.CounterPlus)
        if (userChoiceAnswer == questionVocabulary.hiragana){
            viewModel.onEvent(VocabularyExamEvent.CorrectPlus)
        }else{
            viewModel.onEvent(VocabularyExamEvent.WrongPlus)
            AlertDialog.Builder(requireContext())
                .setView(getCustomAlertView())
                .setTitle(getResourcesString(R.string.practice_alert_title)?:"")
                .setPositiveButton(getResourcesString(R.string.practice_alert_ok)?:""
                ) { _, _ ->
                }
                .setOnCancelListener {
                }.show()
        }

        viewModel.onEvent(VocabularyExamEvent.NextQuestion(examVocabularys.random()))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getCustomAlertView() : View {
        val alertBinding = PracticeAlertviewBinding.inflate(layoutInflater)
        alertBinding.apply {
            alertYourRoma.text = ""
            alertYourWord.text = questionVocabulary.wrongWord
            alertCurrentRoma.text = if (questionVocabulary.word.isNotEmpty()) questionVocabulary.word else ""
            alertCurrentWord.text = questionVocabulary.hiragana
        }
        return alertBinding.root
    }


}


