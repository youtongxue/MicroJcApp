package com.service.microjc.stType;

import java.util.List;

public class RoomResultInfo {
    public int sumRooms;//总数量
    public List <roomsInfo> roomsInfoArrayList;//

    public int getSumRooms() {
        return sumRooms;
    }

    public void setSumRooms(int sumRooms) {
        this.sumRooms = sumRooms;
    }

    public List<roomsInfo> getRoomsInfoArrayList() {
        return roomsInfoArrayList;
    }

    public void setRoomsInfoArrayList(List<roomsInfo> roomsInfoArrayList) {
        this.roomsInfoArrayList = roomsInfoArrayList;
    }

    public static class roomsInfo {
        private String roomNum;//
        private String roomName;//
        private String roomType;//
        private String roomSchool;//
        private String roomSize;//
        private String roomUser;//
        private String roomExamSize;//
        private String roomUseInfo;//

        public String getRoomNum() {
            return roomNum;
        }

        public void setRoomNum(String roomNum) {
            this.roomNum = roomNum;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public String getRoomSchool() {
            return roomSchool;
        }

        public void setRoomSchool(String roomSchool) {
            this.roomSchool = roomSchool;
        }

        public String getRoomSize() {
            return roomSize;
        }

        public void setRoomSize(String roomSize) {
            this.roomSize = roomSize;
        }

        public String getRoomUser() {
            return roomUser;
        }

        public void setRoomUser(String roomUser) {
            this.roomUser = roomUser;
        }

        public String getRoomExamSize() {
            return roomExamSize;
        }

        public void setRoomExamSize(String roomExamSize) {
            this.roomExamSize = roomExamSize;
        }

        public String getRoomUseInfo() {
            return roomUseInfo;
        }

        public void setRoomUseInfo(String roomUseInfo) {
            this.roomUseInfo = roomUseInfo;
        }
    }
}
