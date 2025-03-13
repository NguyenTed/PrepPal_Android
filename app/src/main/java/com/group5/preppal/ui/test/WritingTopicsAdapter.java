package com.group5.preppal.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.group5.preppal.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WritingTopicsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> topicList;
    private HashMap<String, List<Map<String, String>>> topicMap;

    public WritingTopicsAdapter(Context context, List<String> topicList, HashMap<String, List<Map<String, String>>> topicMap) {
        this.context = context;
        this.topicList = topicList;
        this.topicMap = topicMap;
    }

    @Override
    public int getGroupCount() {
        return topicList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String topic = topicList.get(groupPosition);
        return topicMap.get(topic).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return topicList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String topic = topicList.get(groupPosition);
        return topicMap.get(topic).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String topicTitle = topicList.get(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_writing_topic, parent, false);
        }

        TextView tvTopic = convertView.findViewById(R.id.tvTopicTitle);
        tvTopic.setText(topicTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_writing_task, parent, false);
        }

        // Gán tên theo thứ tự: Task 1, Task 2, Task 3...
        String taskTitle = "Task " + (childPosition + 1);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(taskTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
