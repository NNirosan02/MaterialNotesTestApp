package com.nnirosan.materialnotes.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nnirosan.materialnotes.R

abstract class SwipeToDeleteCallback(context: Context, deleteIcon: Drawable, editIcon: Drawable) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var swipeBackgroundDelete: Drawable = context.getDrawable(R.drawable.swiped_background_deleted)!!
    private var swipeBackgroundEdit: Drawable = context.getDrawable(R.drawable.swiped_background_edited)!!
    private var deleteIcon: Drawable = deleteIcon
    private var editIcon: Drawable = editIcon

    //Stop moving items up/down
    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    // Draw delete view
    override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
        if (dX > 0) {
            // Swipe RIGHT
            swipeBackgroundDelete.setBounds(
                    itemView.left,
                    itemView.top,
                    dX.toInt(),
                    itemView.bottom
            )
            deleteIcon.setBounds(
                    itemView.left + iconMargin,
                    itemView.top + iconMargin,
                    itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMargin
            )
            swipeBackgroundDelete.draw(c)
        } else {
            // Swipe LEFT
            swipeBackgroundEdit.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
            )
            editIcon.setBounds(
                    itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                    itemView.top + iconMargin,
                    itemView.right - iconMargin,
                    itemView.bottom - iconMargin
            )
            swipeBackgroundEdit.draw(c)
        }
        c.save()
        if (dX > 0) {
            c.clipRect(itemView.left,
                    itemView.top,
                    dX.toInt(),
                    itemView.bottom)
            deleteIcon.draw(c)
        } else {
            c.clipRect(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)
            editIcon.draw(c)
        }

        c.restore()

        super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
        )
    }

}