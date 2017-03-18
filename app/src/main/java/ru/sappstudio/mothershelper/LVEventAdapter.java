package ru.sappstudio.mothershelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 04.03.17.
 */
public class LVEventAdapter extends BaseAdapter{
    Context _context;
    LayoutInflater lInflater;
    ArrayList<LVEvent> objects;
    final String LOG_TAG = "LVEventAdapter";

    LVEventAdapter(Context context, ArrayList<LVEvent> products) {
        _context = context;
        objects = products;
        lInflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_list_of_events, parent, false);
        }

        final LVEvent p = getEvent(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((ImageView) view.findViewById(R.id.ivEvent)).setImageResource(p.ivEvent);
        ((ImageView) view.findViewById(R.id.ivLVFon)).setImageResource(p.ivLVFon);
        ((TextView) view.findViewById(R.id.tvEvent)).setText(p.tvEvent);
        ((TextView) view.findViewById(R.id.tvTimeEvent)).setText(p.tvTimeEvent);
        ((TextView) view.findViewById(R.id.tvStartEvent)).setText(p.tvStarEvent);
        ((TextView) view.findViewById(R.id.tvStopEvent)).setText(p.tvStopEvent);
        ((TextView) view.findViewById(R.id.tvLVMess)).setText(p.tvLVMess);
        ((ImageView) view.findViewById(R.id.ivStatusEvent)).setImageResource(p.ivStatusEvent);

        ((TextView) view.findViewById(R.id.tvLVMess)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, "OnClick");
                Intent intent = new Intent((((Activity) _context)), MessActivity.class);

                intent.putExtra("_tvEvent", p.tvEvent);
                intent.putExtra("_tableName", p.tableName);
                intent.putExtra("_id", p.id);
                intent.putExtra("_tvLVMess", p.tvLVMess);
                intent.putExtra("_unixTimeSecond_s", Long.valueOf(p.unixTimeSecond_s));
                intent.putExtra("_unixTimeSecond_e", Long.valueOf(p.unixTimeSecond_e));

                ((Activity) _context).startActivity(intent);
            }
        });
        return view;
    }

    // товар по позиции
    LVEvent getEvent(int position) {
        return ((LVEvent) getItem(position));
    }

//    // содержимое корзины
//    ArrayList<Product> getBox() {
//        ArrayList<Product> box = new ArrayList<Product>();
//        for (Product p : objects) {
//            // если в корзине
//            if (p.box)
//                box.add(p);
//        }
//        return box;
//    }
//
//    // обработчик для чекбоксов
//    OnCheckedChangeListener myCheckChangeList = new OnCheckedChangeListener() {
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//            // меняем данные товара (в корзине или нет)
//            getProduct((Integer) buttonView.getTag()).box = isChecked;
//        }
//    };
}
