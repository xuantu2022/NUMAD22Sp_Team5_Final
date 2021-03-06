package edu.neu.madcourse.numad22sp_team5.Support;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;

//@source https://github.com/Xigong93/TimeLineDecorator/blob/master/timeline/src/main/java/pokercc/android/timeline/ThroughTimeLineDecorator2.java

public class ThroughTimeLineDecorator extends RecyclerView.ItemDecoration {
    /**
     * 小圆头的drawable
     */
    private final Drawable headDrawable;
    /**
     * 下面的线的drawable
     */
    private final Drawable lineDrawable;


    private final int width;

    private final Rect mBounds = new Rect();

    private final HeadDrawableLocationMeasurer headDrawableLocationMeasurer;
    private final int paddingLeft;
    private ItemTypeHandler typeHandler;

    /**
     * 最后一个item的下半段线底部的距离
     */
    private int lastLineMarginBottom;
    /**
     * 是否要绘制最后一个条目的下半段线
     */
    private boolean drawLastItemBottomLine = true;

    /**
     * @param headDrawable          小圆头的drawable
     * @param lineDrawable          下面的线的drawable
     * @param paddingLeft           左边的padding
     * @param paddingRight          右边的padding
     * @param headDrawableMarginTop 头的margin
     */
    public ThroughTimeLineDecorator(Drawable headDrawable, Drawable lineDrawable, int paddingLeft, int paddingRight, final int headDrawableMarginTop) {
        this(headDrawable, lineDrawable, paddingLeft, paddingRight, new HeadDrawableLocationMeasurer() {
            @Override
            public int getHeadDrawableMarginTop(View childView) {
                return headDrawableMarginTop;
            }
        });
    }


    /**
     * @param headDrawable                 小圆头的drawable
     * @param lineDrawable                 下面的线的drawable
     * @param paddingLeft                  水平方向的padding
     * @param headDrawableLocationMeasurer 自定义头的位置测量器
     */
    public ThroughTimeLineDecorator(@NonNull Drawable headDrawable, @NonNull Drawable lineDrawable, int paddingLeft, int paddingRight, @NonNull HeadDrawableLocationMeasurer headDrawableLocationMeasurer) {
        this.headDrawable = headDrawable;
        this.lineDrawable = lineDrawable;
        this.headDrawableLocationMeasurer = headDrawableLocationMeasurer;
        this.paddingLeft = paddingLeft;
        // 宽度直接通过HeadDrawable 计算出来的
        this.width = headDrawable.getIntrinsicWidth() + paddingLeft + paddingRight;
    }

    /**
     * 是否要绘制最后一个条目的下半段线(默认为true)
     *
     * @param drawLastItemBottomLine
     */
    public void setDrawLastItemBottomLine(boolean drawLastItemBottomLine) {
        this.drawLastItemBottomLine = drawLastItemBottomLine;
    }

    /**
     * 最后一个item的下半段线底部的距离(默认为0)
     *
     * @param lastLineMarginBottom
     */
    public void setLastLineMarginBottom(@Px int lastLineMarginBottom) {
        this.lastLineMarginBottom = lastLineMarginBottom;
    }

    public void setTypeHandler(ItemTypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager != null) {
            final int left;
            final int right;
            //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                c.clipRect(left, parent.getPaddingTop(), right,
                        parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            final boolean drawLastItem = drawLast(parent);
            final int saveCount = c.save();
            int childCount = layoutManager.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = layoutManager.getChildAt(i);
                if (child != null) {
                    // 跳过不绘制的条目
                    if (typeHandler != null) {
                        if (typeHandler.skipItem(parent, layoutManager.getPosition(child))) {
                            continue;
                        }
                    }

                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    final int top = mBounds.top + Math.round(child.getTranslationY());
                    final int headWidth = this.headDrawable.getIntrinsicWidth();
                    final int headLeft = left + this.paddingLeft;
                    final int headDrawableMarginTop = this.headDrawableLocationMeasurer.getHeadDrawableMarginTop(child);
                    this.headDrawable.setBounds(headLeft, top + headDrawableMarginTop, headLeft + headWidth, top + headDrawableMarginTop + this.headDrawable.getIntrinsicHeight());
                    this.headDrawable.draw(c);
                    final int lineWidth = this.lineDrawable.getIntrinsicWidth();
                    final int lineLeft = this.headDrawable.getBounds().centerX() - (lineWidth >> 1);
                    final int lineRight = lineLeft + lineWidth;


                    // 绘制上半段线
                    // 1. 第一条不绘制
                    // 2. 如果当前不是第一条，且上一条没有绘制，那么这一条也不绘制
                    int childPosition = layoutManager.getPosition(child);
                    if (childPosition != 0) {
                        boolean lastItemSkipDraw = true;
                        if (typeHandler != null) {
                            lastItemSkipDraw = typeHandler.skipItem(parent, layoutManager.getPosition(child) - 1);
                        }
                        if (!lastItemSkipDraw) {
                            this.lineDrawable.setBounds(lineLeft, top, lineRight, this.headDrawable.getBounds().top);
                            this.lineDrawable.draw(c);
                        }
                    }

                    //绘制下半段线
                    // 当前为最后一条，或为倒数第二条且不绘制最后一条时，不绘制最下面的那条线
                    if (childPosition == layoutManager.getItemCount() - 1 || childPosition == layoutManager.getItemCount() - 2 && !drawLastItem) {
                        if (drawLastItemBottomLine) {
                            this.lineDrawable.setBounds(lineLeft, this.headDrawable.getBounds().bottom, lineRight, top + mBounds.height() - lastLineMarginBottom);
                            this.lineDrawable.draw(c);
                        }

                        break;
                    } else {
                        this.lineDrawable.setBounds(lineLeft, this.headDrawable.getBounds().bottom, lineRight, top + mBounds.height());
                        this.lineDrawable.draw(c);

                    }

                }
            }
            c.restoreToCount(saveCount);

        }

    }

    private boolean drawLast(@NonNull RecyclerView parent) {
        boolean drawLastItem = true;
        if (typeHandler != null) {
            drawLastItem = typeHandler.drawLastItem(parent);
        }
        return drawLastItem;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        if (parent.getChildAdapterPosition(view) != state.getItemCount() - 1 ||
//                drawLast(parent)) {
//            outRect.left = width;
//        }
    }

    public interface HeadDrawableLocationMeasurer {
        int getHeadDrawableMarginTop(View childView);
    }

    public abstract class ItemTypeHandler {
        /**
         * 是否需要显示最后一个,比如最后一条是下拉刷新，则不绘制
         *
         * @param @NonNull RecyclerView parent
         * @return 是否需要绘制装饰器
         */
        public abstract boolean drawLastItem(@NonNull RecyclerView parent);

        /**
         * 是否跳过这条的绘制
         *
         * @param parent
         * @param position
         * @return
         */
        public boolean skipItem(RecyclerView parent, int position) {
            return false;
        }
    }
}


