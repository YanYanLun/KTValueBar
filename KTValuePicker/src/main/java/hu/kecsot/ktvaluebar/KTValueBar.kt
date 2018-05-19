package hu.kecsot.ktvaluebar

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.kecsot.mylibrary.R
import hu.kecsot.ktvaluebar.button.KTButton
import hu.kecsot.ktvaluebar.valuebar.listener.KTValueBarChangedListener
import kotlinx.android.synthetic.main.ktvaluebar_layout.view.*


class KTValueBar : LinearLayout {

    private var leftButtonLayout: FrameLayout? = null
    private var rightButtonLayout: FrameLayout? = null
    private var rightButtonView: KTButton? = null
    private var leftButtonView: KTButton? = null
    private var parameters = KTValueBarParams.Builder().build()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.ktvaluebar_layout, this)

        attrs?.let {
            // Create a default builder to get default values, if any attrs is setted then override it
            val defaultParameters = KTValueBarParams.Builder().build()
            val typedArray = context.theme.obtainStyledAttributes(it, R.styleable.KTValueBar, 0, 0)
            parameters.apply {
                minValue = typedArray.getFloat(R.styleable.KTValueBar_minValue, defaultParameters.minValue)
                maxValue = typedArray.getFloat(R.styleable.KTValueBar_maxValue, defaultParameters.maxValue)
                stepValue = typedArray.getFloat(R.styleable.KTValueBar_stepValue, defaultParameters.stepValue)
                actualValue = typedArray.getFloat(R.styleable.KTValueBar_actualValue, defaultParameters.actualValue)

                valueBarColor = typedArray.getColor(R.styleable.KTValueBar_valueBarColor, defaultParameters.valueBarColor)
                valueBarBackgroundColor = typedArray.getColor(R.styleable.KTValueBar_valueBarBackgroundColor, defaultParameters.valueBarBackgroundColor)
                isTouchEnabled = typedArray.getBoolean(R.styleable.KTValueBar_isTouchEnabled, defaultParameters.isTouchEnabled)

                horizontalBorderColor = typedArray.getColor(R.styleable.KTValueBar_horizontalBorderColor, defaultParameters.horizontalBorderColor)
                isHorizontalBorderNeeded = typedArray.getBoolean(R.styleable.KTValueBar_horizontalBorderNeeded, defaultParameters.isHorizontalBorderNeeded)
                horizontalBorderWidth = typedArray.getDimension(R.styleable.KTValueBar_horizontalBorderWidth, defaultParameters.horizontalBorderWidth)
                isOnlyRemainingHorizontalBorder = typedArray.getBoolean(R.styleable.KTValueBar_horizontalBorderOnlyRemaining, defaultParameters.isOnlyRemainingHorizontalBorder)

                verticalBorderColor = typedArray.getColor(R.styleable.KTValueBar_verticalBorderColor, defaultParameters.verticalBorderColor)
                isVerticalBorderNeeded = typedArray.getBoolean(R.styleable.KTValueBar_verticalBorderNeeded, defaultParameters.isVerticalBorderNeeded)
                verticalBorderWidth = typedArray.getDimension(R.styleable.KTValueBar_verticalBorderWidth, defaultParameters.verticalBorderWidth)
                val typeOfValueBar = typedArray.getInteger(R.styleable.KTValueBar_valueBarType, 0)

                this.typeOfValueBar = when (typeOfValueBar) {
                    0 -> TypeOfValueBar.NEXT_TO_VALUEBAR
                    1 -> TypeOfValueBar.TOP_OF_VALUEBAR
                    else -> TypeOfValueBar.BOTTOM_OF_VALUEBAR
                }

                val buttonCornerRadius = typedArray.getDimension(R.styleable.KTValueBar_buttonCornerRadius, defaultParameters.rightButtonParams.cornerRadius)

                rightButtonParams.apply {
                    this.backgroundColor = typedArray.getColor(R.styleable.KTValueBar_rightButtonBackgroundColor, defaultParameters.rightButtonParams.backgroundColor)
                    this.drawable = ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.KTValueBar_rightButtonDrawable, KTButton.DEFAULT_RIGHT_DRAWABLE))
                    this.drawableColor = typedArray.getColor(R.styleable.KTValueBar_rightButtonDrawableColor, -1)
                    this.drawableTopPadding = typedArray.getDimension(R.styleable.KTValueBar_rightButtonTopPadding, defaultParameters.rightButtonParams.drawableTopPadding)
                    this.drawableBottomPadding = typedArray.getDimension(R.styleable.KTValueBar_rightButtonBottomPadding, defaultParameters.rightButtonParams.drawableBottomPadding)
                    this.drawableLeftPadding = typedArray.getDimension(R.styleable.KTValueBar_rightButtonLeftPadding, defaultParameters.rightButtonParams.drawableLeftPadding)
                    this.drawableRightPadding = typedArray.getDimension(R.styleable.KTValueBar_rightButtonRightPadding, defaultParameters.rightButtonParams.drawableRightPadding)
                    this.cornerRadius = buttonCornerRadius
                }

                leftButtonParams.apply {
                    this.backgroundColor = typedArray.getColor(R.styleable.KTValueBar_leftButtonBackgroundColor, defaultParameters.leftButtonParams.backgroundColor)
                    this.drawable = ContextCompat.getDrawable(context, typedArray.getResourceId(R.styleable.KTValueBar_leftButtonDrawable, KTButton.DEFAULT_LEFT_DRAWABLE))
                    this.drawableColor = typedArray.getColor(R.styleable.KTValueBar_leftButtonDrawableColor, -1)
                    this.drawableTopPadding = typedArray.getDimension(R.styleable.KTValueBar_leftButtonTopPadding, defaultParameters.leftButtonParams.drawableTopPadding)
                    this.drawableBottomPadding = typedArray.getDimension(R.styleable.KTValueBar_leftButtonBottomPadding, defaultParameters.leftButtonParams.drawableBottomPadding)
                    this.drawableLeftPadding = typedArray.getDimension(R.styleable.KTValueBar_leftButtonLeftPadding, defaultParameters.leftButtonParams.drawableLeftPadding)
                    this.drawableRightPadding = typedArray.getDimension(R.styleable.KTValueBar_leftButtonRightPadding, defaultParameters.leftButtonParams.drawableRightPadding)
                    this.cornerRadius = buttonCornerRadius
                }
            }
        }

        valueBar.changeParams(parameters)
        loadButtonViews()
    }

    /**
     * To change the settings Programmatically
     */
    fun setParams(parameters: KTValueBarParams) {
        this.parameters = parameters
        valueBar.changeParams(parameters)
        loadButtonViews()
    }

    /**
     * Get the actually parameters
     */
    fun getParams(): KTValueBarParams {
        return parameters
    }

    /**
     * Clear the old buttons and build the new buttons
     */
    private fun loadButtonViews() {
        // Recycle old buttons if exist
        recycleButtons()

        // Find the actually needed Views
        when (parameters.typeOfValueBar) {
            TypeOfValueBar.NEXT_TO_VALUEBAR -> {
                rightButtonLayout = rightCenterViewGroup
                leftButtonLayout = leftCenterViewGroup
            }
            TypeOfValueBar.TOP_OF_VALUEBAR -> {
                rightButtonLayout = rightTopViewGroup
                leftButtonLayout = leftTopViewGroup
            }
            TypeOfValueBar.BOTTOM_OF_VALUEBAR -> {
                rightButtonLayout = rightBottomViewGroup
                leftButtonLayout = leftBottomViewGroup
            }
        }

        rightButtonView = KTButton(context, false, parameters.typeOfValueBar, parameters.rightButtonParams)
        leftButtonView = KTButton(context, true, parameters.typeOfValueBar, parameters.leftButtonParams)

        // Build and show the new buttons
        leftButtonLayout?.apply {
            addView(leftButtonView)
            visibility = View.VISIBLE
        }
        rightButtonLayout?.apply {
            addView(rightButtonView)
            visibility = View.VISIBLE
        }

        leftButtonLayout?.setOnClickListener({
            valueBar.decreaseValue()
        })

        rightButtonLayout?.setOnClickListener({
            valueBar.increaseValue()
        })
    }

    fun setValueBarChangedListener(listenerKT: KTValueBarChangedListener) {
        valueBar.setValueBarChangedListener(listenerKT)
    }

    /**
     * Set the old views to gone and remove the buttons from the view
     */
    private fun recycleButtons() {
        rightButtonLayout?.removeView(rightButtonView)
        rightButtonLayout?.visibility = View.GONE
        rightButtonLayout = null
        leftButtonLayout?.removeView(leftButtonView)
        leftButtonLayout?.visibility = View.GONE
        leftButtonLayout = null
    }

    fun callOnValueChangeListener() {
        valueBar.callOnValueChangeListener()
    }

    fun animate(from: Float, to: Float, durationMillis: Int) {
        valueBar.animate(from, to, durationMillis)
    }

    fun animateUp(to: Float, durationMillis: Int) {
        valueBar.animateUp(to, durationMillis)
    }

    fun animateDown(to: Float, durationMillis: Int) {
        valueBar.animateDown(to, durationMillis)
    }

    enum class TypeOfValueBar {
        NEXT_TO_VALUEBAR,
        TOP_OF_VALUEBAR,
        BOTTOM_OF_VALUEBAR
    }
}
