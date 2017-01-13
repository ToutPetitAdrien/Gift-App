//package com.example.adrien.gift_app;
//
//import android.util.Log;
//import android.widget.Filter;
//
//import java.util.ArrayList;
//
//
//public class IdeaFilter extends Filter {
//
//
////    protected FilterResults performFiltering(CharSequence constraint) {
////        Log.d("IdeasAdapter", "Coucou de getFilter performFiltering");
////        FilterResults results = new FilterResults();
////        ArrayList<Idea> FilteredArrayIdeas = new ArrayList<Idea>();
////
////        if(constraint == null || constraint.length() == 0){
////            results.count = ideasArray.size();
////            results.values = ideasArray;
////        } else {
////            constraint = constraint.toString().toLowerCase();
////            for(int i=0; i < ideasArray.size(); i++){
////                Log.d("IdeasAdapter", "Coucou de getFilter performFiltering loop");
////                Idea ideaCursor = ideasArray.get(i);
////                if(ideaCursor.getTitle().toLowerCase().startsWith(constraint.toString())){
////                    FilteredArrayIdeas.add(ideaCursor);
////                }
////            }
////            results.count = FilteredArrayIdeas.size();
////            results.values = FilteredArrayIdeas;
////        }
////        return results;
////    }
////
////    @Override
////    protected void publishResults(CharSequence constraint, FilterResults results) {
////        Log.d("IdeasAdapter", "Coucou de getFilter publishResults");
////        if(results.count != 0){
//////                    ideasArray = (ArrayList<Idea>)results.values;
////
////            notifyDataSetChanged();
////        }
////
////    }
//
//}
