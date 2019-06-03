package com.mradmin.nasa_android.db;

import com.mradmin.nasa_android.model.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DatabaseService {

    public <T extends BaseModel> boolean saveItem(T item) {
        List<T> list = new ArrayList<>(1);
        list.add(item);
        return saveItems(list);
    }

    public <T extends BaseModel> boolean saveItems(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }

        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();
            return true;
        } catch (Exception ex) {
            System.out.println("------------------- exception save: " + ex + " ______ " + ex.getLocalizedMessage());
            realm.cancelTransaction();
            return false;
        } finally {
            realm.close();
        }
    }

    public <E extends BaseModel> List<E> getItemList(Class<E> clazz) {
        Realm realm = Realm.getDefaultInstance();

        try {
            RealmResults<E> realmList = realm.where(clazz).findAll();
            return realm.copyFromRealm(realmList);
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            realm.close();
        }
    }

    public <E extends BaseModel> E getItem(Class<E> clazz) {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<E> items = realm
                    .where(clazz)
                    .findAll();
            if (items.size() == 1) {
                return realm.copyFromRealm(items).get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            realm.close();
        }
    }

    public <E extends BaseModel> boolean deleteItems(Class<E> clazz) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.delete(clazz);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            return false;
        } finally {
            realm.close();
        }

        return true;
    }

    public <E extends BaseModel> boolean deleteItems(Class<E> clazz, String[] values) {
        if (values == null || values.length == 0) {
            return false;
        }

        boolean isDeleted;
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();

            isDeleted = Realm.getDefaultInstance()
                    .where(clazz)
                    .in(BaseModel.ID_COLUMN, values)
                    .findAll()
                    .deleteAllFromRealm();

            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            return false;
        } finally {
            realm.close();
        }

        return isDeleted;
    }

    public <T extends BaseModel> boolean deleteItem(T baseModel) {
        boolean isDeleted;
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.beginTransaction();

            isDeleted = Realm.getDefaultInstance()
                    .where(baseModel.getClass())
                    .equalTo(BaseModel.ID_COLUMN, baseModel.getId())
                    .findAll()
                    .deleteFirstFromRealm();

            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
            return false;
        } finally {
            realm.close();
        }

        return isDeleted;
    }

    public void clearAll() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        } catch (Exception e) {
            realm.cancelTransaction();
        } finally {
            realm.close();
        }
    }
}
