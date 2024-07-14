package chen.example.lee.jppractice.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.lee.jppractice.R


class FuriganaView : AppCompatTextView {
    private var mTextAlignment = View.TEXT_ALIGNMENT_CENTER
    private var mMaxLines = -1
    private var mSideMargins = 0
    private var mMaxLineWidth = 0f
    private var mLineHeight = 0f
    private var mTextSize = 0f
    private var mFuriganaSize = 0f
    private var mLineSpacing = -1f
    private var mNormalPaint: TextPaint? = null
    private var mFuriganaPaint: TextPaint? = null
    private var mLines: ArrayList<Line>? = null
    private var mText: String? = null

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        val textPaint = paint
        mTextSize = textPaint.textSize
        mNormalPaint = TextPaint(textPaint)
        mFuriganaPaint = TextPaint(textPaint)
        mFuriganaSize = mTextSize / 2.0f
        if (attrs != null) {

            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.FuriganaView)
            mText = a.getString(R.styleable.FuriganaView_android_text)
            mMaxLines = a.getInt(R.styleable.FuriganaView_android_maxLines, -1)
            mTextAlignment = a.getInt(R.styleable.FuriganaView_android_textAlignment, 2)
            mFuriganaSize = a.getDimension(R.styleable.FuriganaView_furiganaSize, mFuriganaSize)
            mLineSpacing = a.getDimension(
                R.styleable.FuriganaView_android_lineSpacingExtra,
                mFuriganaSize / 2.0f
            )
            val marginLeftRight = a.getDimension(
                R.styleable.FuriganaView_android_layout_marginLeft,
                0f
            ) + a.getDimension(R.styleable.FuriganaView_android_layout_marginRight, 0f)
            val marginEndStart = a.getDimension(
                R.styleable.FuriganaView_android_layout_marginEnd,
                0f
            ) + a.getDimension(R.styleable.FuriganaView_android_layout_marginStart, 0f)
            mSideMargins = Math.ceil(
                Math.max(
                    marginEndStart,
                    marginLeftRight
                ).toDouble()
            ).toInt()
            a.recycle()
        }
        mFuriganaPaint!!.textSize = mFuriganaSize
        mLineHeight = mTextSize + mFuriganaSize + mLineSpacing
    }

    fun setText(text: String?) {
        mText = text
        mLines = null
        requestLayout()
    }

    override fun setTextAlignment(textAlignment: Int) {
        mTextAlignment = textAlignment
        invalidate()
    }

    override fun setTextSize(size: Float) {
        mNormalPaint!!.textSize = size
        requestLayout()
    }

    fun setFuriganaSize(size: Float) {
        mFuriganaPaint!!.textSize = size
        requestLayout()
    }

    fun setLineSpacing(spacing: Float) {
        mLineSpacing = spacing
        mLineHeight = mTextSize + mFuriganaSize + mLineSpacing
        requestLayout()
    }

    override fun getText(): String {
        return mText!!
    }

    override fun onDraw(canvas: Canvas) {
        if (mLines != null) {
            var y = mLineHeight
            for (i in mLines!!.indices) {
                mLines!![i].onDraw(canvas, y)
                y += mLineHeight
                if (mMaxLines != -1 && i == mMaxLines - 1) break
            }
        } else super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width: Int
        var height: Int
        width =
            if (widthMode == MeasureSpec.EXACTLY && widthSize != 0) widthSize else if (widthMode == MeasureSpec.AT_MOST) measureWidth(
                widthSize - mSideMargins
            ) else measureWidth(-1)
        mMaxLineWidth = width.toFloat()
        if (width > 0) handleText()
        var maxHeight =
            if (mLines != null) Math.ceil(mLines!!.size.toFloat() * mLineHeight.toDouble()).toInt() else 0
        if (mMaxLines != -1) maxHeight =
            mMaxLines * Math.ceil(mLineHeight.toDouble()).toInt()
        height =
            if (heightMode == MeasureSpec.EXACTLY) heightSize else if (heightMode == MeasureSpec.AT_MOST) if (maxHeight < heightSize) maxHeight else heightSize else maxHeight
        if (heightMode != MeasureSpec.UNSPECIFIED && maxHeight > heightSize) height =
            height or View.MEASURED_STATE_TOO_SMALL
        setMeasuredDimension(width, height)
    }

    //measures the longest line in the text
    private fun measureWidth(width: Int): Int {
        if (mText == null || mText!!.isEmpty()) return 0
        var text = mText!!.replace(TAG_REGEX.toRegex(), "")
        var normal = ""
        var maxLength = 0.0f
        var length = 0.0f
        while (!text.isEmpty()) {
            if (text.indexOf(LINE_BREAK) == 0 || text.indexOf("\n") == 0) {
                length += mNormalPaint!!.measureText(normal)
                maxLength = if (length > maxLength) length else maxLength
                length = 0.0f
                text = text.substring(1)
                normal = ""
            } else if (text.indexOf(FURIGANA_START) == 0) {
                if (!text.contains(FURIGANA_MIDDLE) || !text.contains(
                        FURIGANA_END
                    )
                ) {
                    text = text.substring(1)
                    continue
                }
                val middle = text.indexOf(FURIGANA_MIDDLE)
                val end = text.indexOf(FURIGANA_END)
                if (end < middle) {
                    text = text.substring(1)
                    continue
                }
                val kanji = mNormalPaint!!.measureText(text.substring(1, middle))
                val kana = mFuriganaPaint!!.measureText(text.substring(middle + 1, end))
                text = text.substring(text.indexOf(FURIGANA_END) + 1)
                length += Math.max(kanji, kana)
            } else {
                normal += text.substring(0, 1)
                text = text.substring(1)
            }
        }
        length += mNormalPaint!!.measureText(normal)
        maxLength = if (length > maxLength) length else maxLength
        val result = Math.ceil(maxLength.toDouble()).toInt()
        if (width < 0) return result
        return if (result < width) result else width
    }

    //breaks the text into lines shorter than the maximum length
    private fun handleText() {
        if (mText == null || mText!!.isEmpty()) return
        var text: String = mText as String
        mLines = ArrayList()
        var isBold = false
        var isItalic = false
        var isUnderlined = false
        var line = Line()
        var normalHandler: NormalTextHolder? = null
        while (!text.isEmpty()) {
            if (text.indexOf(BOLD_START) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isBold = true
                text = text.substring(3)
            } else if (text.indexOf(BOLD_END) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isBold = false
                text = text.substring(4)
            } else if (text.indexOf(ITALIC_START) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isItalic = true
                text = text.substring(3)
            } else if (text.indexOf(ITALIC_END) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isItalic = false
                text = text.substring(4)
            } else if (text.indexOf(UNDERLINE_START) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isUnderlined = true
                text = text.substring(3)
            } else if (text.indexOf(UNDERLINE_END) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                isUnderlined = false
                text = text.substring(4)
            } else if (text.indexOf(LINE_BREAK) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                text = text.substring(4)
                mLines!!.add(line)
                line = Line()
            } else if (text.indexOf("\n") == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                text = text.substring(1)
                mLines!!.add(line)
                line = Line()
            } else if (text.indexOf(FURIGANA_START) == 0) {
                if (normalHandler != null) line.add(normalHandler.endText())
                if (!text.contains(FURIGANA_MIDDLE) || !text.contains(
                        FURIGANA_END
                    )
                ) {
                    text = text.substring(1)
                    continue
                }
                val middle = text.indexOf(FURIGANA_MIDDLE)
                val end = text.indexOf(FURIGANA_END)
                if (end < middle) {
                    text = text.substring(1)
                    continue
                }
                val kanji = text.substring(1, middle)
                    .replace(BREAK_REGEX.toRegex(), "").replace(
                        TAG_REGEX.toRegex(),
                        ""
                    ) //remove all tags and line breaks
                val kana = text.substring(middle + 1, end)
                    .replace(BREAK_REGEX.toRegex(), "")
                    .replace(TAG_REGEX.toRegex(), "")
                text = text.substring(text.indexOf(FURIGANA_END) + 1)
                val pair =
                    PairedText(kanji, kana, isBold, isItalic, isUnderlined)
                if (pair.width() + line.width() > mMaxLineWidth) {
                    if (!line.isEmpty) (mLines as ArrayList<Line>).add(line)
                    line = Line()
                }
                line.add(pair)
            } else {
                if (normalHandler == null) normalHandler = NormalTextHolder()
                if (normalHandler.test(text.substring(0, 1), line)) {
                    line.add(normalHandler.endText())
                    mLines!!.add(line)
                    line = Line()
                }
                normalHandler.expand(text.substring(0, 1), isBold, isItalic, isUnderlined)
                text = text.substring(1)
            }
        }
        if (normalHandler != null) line.add(normalHandler.endText())
        if (!line.isEmpty) mLines!!.add(line)
    }

    //sets how much space should be in the start of the line
    private fun handleNewline(width: Float): Float {
        val remainder = mMaxLineWidth - width
        if (remainder > 0) when (mTextAlignment) {
            View.TEXT_ALIGNMENT_CENTER -> return remainder / 2.0f
            View.TEXT_ALIGNMENT_TEXT_END -> return remainder
        }
        return 0.0f
    }

    private inner class NormalTextHolder internal constructor() {
        private var normal = ""
        private var bold = false
        private var italic = false
        private var underlined = false
        fun test(test: String, line: Line): Boolean {
            var width = mNormalPaint!!.measureText(normal + test) + line.width()
            if (test == "。") //reduces lines with just a period
                width -= mTextSize * 0.7f
            return width > mMaxLineWidth
        }

        fun expand(
            text: String,
            isBold: Boolean,
            isItalic: Boolean,
            isUnderlined: Boolean
        ) {
            normal += text
            bold = isBold
            italic = isItalic
            underlined = isUnderlined
        }

        fun endText(): PairedText {
            val pair =
                PairedText(normal, null, bold, italic, underlined)
            normal = ""
            return pair
        }

    }

    private inner class Line internal constructor() {
        private var width = 0.0f
        private val pairs: MutableList<PairedText>
        val isEmpty: Boolean
            get() = pairs.isEmpty()

        fun width(): Float {
            return width
        }

        fun add(pairText: PairedText) {
            pairs.add(pairText)
            width += pairText.width()
        }

        fun onDraw(canvas: Canvas, y: Float) {
            var x = handleNewline(width)
            for (i in pairs.indices) {
                pairs[i].onDraw(canvas, x, y)
                x += pairs[i].width()
            }
        }

        init {
            pairs = ArrayList()
        }
    }

    private inner class PairedText internal constructor(
        private val normalText: String,
        private val furiganaText: String?,
        isBold: Boolean,
        isItalic: Boolean,
        isUnderlined: Boolean
    ) {
        private var width = 0f
        private var normalWidth = 0f
        private var furiganaWidth = 0f
        private var offset = 0f
        private lateinit var normalPaint: TextPaint
        private lateinit var furiganaPaint: TextPaint
        //set paint and calculate spacing between characters
        private fun setPaint(
            bold: Boolean,
            italic: Boolean,
            underlined: Boolean
        ) {
            normalPaint = TextPaint(mNormalPaint)
            normalPaint.isFakeBoldText = bold
            normalPaint.isUnderlineText = underlined
            if (italic) normalPaint.textSkewX = -0.35f
            normalWidth = normalPaint.measureText(normalText)
            if (furiganaText != null) {
                furiganaPaint = TextPaint(mFuriganaPaint)
                furiganaPaint.isFakeBoldText = bold
                if (italic) furiganaPaint.textSkewX = -0.35f
                furiganaWidth = furiganaPaint.measureText(furiganaText)
                offset = if (normalWidth < furiganaWidth) {
                    (furiganaWidth - normalWidth) / (normalText.length + 1)
                } else {
                    (normalWidth - furiganaWidth) / (furiganaText.length + 1)
                }
                width = Math.max(normalWidth, furiganaWidth)
            } else width = normalWidth
        }

        fun width(): Float {
            return width
        }

        fun onDraw(canvas: Canvas, x: Float, y: Float) {
            val y = y.minus(mLineSpacing)
            if (furiganaText == null) {
                normalPaint.color = currentTextColor
                canvas.drawText(normalText, 0, normalText.length, x, y, normalPaint)
            } else {
                normalPaint.color = currentTextColor
                furiganaPaint.color = currentTextColor
                //draw kanji and kana and apply spacing
                if (normalWidth < furiganaWidth) {
                    var offsetX = x + offset
                    for (i in normalText.indices) {
                        canvas.drawText(normalText, i, i + 1, offsetX, y, normalPaint)
                        offsetX += normalPaint.measureText(
                            normalText.substring(
                                i,
                                i + 1
                            )
                        ) + offset
                    }
                    canvas.drawText(
                        furiganaText,
                        0,
                        furiganaText.length,
                        x,
                        y - mTextSize,
                        furiganaPaint
                    )
                } else {
                    var offsetX = x + offset
                    for (i in furiganaText.indices) {
                        canvas.drawText(
                            furiganaText,
                            i,
                            i + 1,
                            offsetX,
                            y - mTextSize,
                            furiganaPaint
                        )
                        offsetX += furiganaPaint.measureText(
                            furiganaText.substring(
                                i,
                                i + 1
                            )
                        ) + offset
                    }
                    canvas.drawText(normalText, 0, normalText.length, x, y, normalPaint)
                }
            }
        }

        init {
            setPaint(isBold, isItalic, isUnderlined)
        }
    }

    companion object {
        private const val FURIGANA_START = "{"
        private const val FURIGANA_MIDDLE = ";"
        private const val FURIGANA_END = "}"
        private const val ITALIC_START = "<i>"
        private const val ITALIC_END = "</i>"
        private const val BOLD_START = "<b>"
        private const val BOLD_END = "</b>"
        private const val UNDERLINE_START = "<u>"
        private const val UNDERLINE_END = "</u>"
        private const val LINE_BREAK = "<br>"
        private const val BREAK_REGEX =
            "(<br>|\n)" //remove line breaks from kanji and furigana
        private const val TAG_REGEX =
            "(<i>|</i>|<b>|</b>|<u>|</u>)" //remove tags from the text and furigana kanji combinations
    }
}