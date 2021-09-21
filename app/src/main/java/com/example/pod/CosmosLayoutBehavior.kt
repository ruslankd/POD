package com.example.pod

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout


class CosmosLayoutBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<ImageView>(context, attrs) {
    private var mCustomFinalYPosition = 0f
    private var mCustomStartXPosition = 0f
    private var mCustomStartToolbarPosition = 0f
    private var mCustomStartHeight = 0f
    private var mCustomFinalHeight = 0f
    private var mAvatarMaxSize = 0f
    private val mFinalLeftAvatarPadding: Float
    private val mStartPosition = 0f
    private var mStartXPosition = 0
    private var mStartToolbarPosition = 0f
    private var mStartYPosition = 0
    private var mFinalYPosition = 0
    private var mStartHeight = 0
    private var mFinalXPosition = 0
    private var mChangeBehaviorPoint = 0f
    private var mContext: Context = context
    private fun init() {
        bindDimensions()
    }

    private fun bindDimensions() {
        mAvatarMaxSize = mContext.resources.getDimension(R.dimen.image_width)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        return dependency is Toolbar
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ImageView, dependency: View): Boolean {
        maybeInitProperties(child, dependency)
        val maxScrollDistance = mStartToolbarPosition.toInt()
        val expandedPercentageFactor = dependency.y / maxScrollDistance
        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            val heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint
            val distanceXToSubtract: Float = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + child.height / 2
            val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + child.height / 2
            child.x = mStartXPosition - distanceXToSubtract
            child.y = mStartYPosition - distanceYToSubtract
            val heightToSubtract = (mStartHeight - mCustomFinalHeight) * heightFactor
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = (mStartHeight - heightToSubtract).toInt()
            lp.height = (mStartHeight - heightToSubtract).toInt()
            child.layoutParams = lp
        } else {
            val distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + mStartHeight / 2
            child.x = (mStartXPosition - child.width / 2).toFloat()
            child.y = mStartYPosition - distanceYToSubtract
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = mStartHeight
            lp.height = mStartHeight
            child.layoutParams = lp
        }
        return true
    }

    private fun maybeInitProperties(child: ImageView, dependency: View) {
        if (mStartYPosition == 0) mStartYPosition = dependency.y.toInt()
        if (mFinalYPosition == 0) mFinalYPosition = dependency.height / 2
        if (mStartHeight == 0) mStartHeight = child.height
        if (mStartXPosition == 0) mStartXPosition = ((child.x + child.width / 2).toInt())
        if (mFinalXPosition == 0) mFinalXPosition = mContext.resources.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + mCustomFinalHeight.toInt() / 2
        if (mStartToolbarPosition == 0f) mStartToolbarPosition = dependency.y
        if (mChangeBehaviorPoint == 0f) {
            mChangeBehaviorPoint = (child.height - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition))
        }
    }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = mContext.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = mContext.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    companion object {
        private const val MIN_AVATAR_PERCENTAGE_SIZE = 0.3f
        private const val EXTRA_FINAL_AVATAR_PADDING = 80
        private const val TAG = "behavior"
    }

    init {
        val a = mContext.obtainStyledAttributes(attrs, R.styleable.CosmosLayoutBehavior)
        mCustomFinalYPosition = a.getDimension(R.styleable.CosmosLayoutBehavior_finalYPosition, 0f)
        mCustomStartXPosition = a.getDimension(R.styleable.CosmosLayoutBehavior_startXPosition, 0f)
        mCustomStartToolbarPosition = a.getDimension(R.styleable.CosmosLayoutBehavior_startToolbarPosition, 0f)
        mCustomStartHeight = a.getDimension(R.styleable.CosmosLayoutBehavior_startHeight, 0f)
        mCustomFinalHeight = a.getDimension(R.styleable.CosmosLayoutBehavior_finalHeight, 0f)
        a.recycle()
        init()
        mFinalLeftAvatarPadding = mContext.resources.getDimension(
                R.dimen.spacing_normal)
    }
}