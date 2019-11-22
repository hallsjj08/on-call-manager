package jordan_jefferson.com.oncallphonemanager;

import android.util.Log;

import androidx.room.TypeConverter;

import java.util.Arrays;

public final class Converters {

    @TypeConverter
    public static boolean[] fromString(String value) {
        String[] parts = value.replaceAll("[\\[\\]]", "").split(", ");

        boolean[] array = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = Boolean.parseBoolean(parts[i]);
        }

        return array;
    }

    @TypeConverter
    public static String fromBooleanArray(boolean[] booleanArray) {
        Log.d("Boolean Converter ", Arrays.toString(booleanArray));
        return Arrays.toString(booleanArray);
    }

}
