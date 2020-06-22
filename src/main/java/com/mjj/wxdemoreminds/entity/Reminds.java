package com.mjj.wxdemoreminds.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjj.wxdemoreminds.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"openId"})
public class Reminds {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String time;
    @Column(nullable = false)
    private Boolean valid;
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private String openId;

    public int getType() {
        return this.type.type;
    }

    public void setType(int type) {
        this.type = Type.getInstance(type);
    }

    public String formatTime() {
        Calendar ca = Calendar.getInstance();
        StringBuilder builder = new StringBuilder("00");
        String[] times = this.time.split(":");
        builder.append(" ").append(times[1]).append(" ").append(times[0]);
        if (this.type.type == Type.ONCE.type || this.type.type == Type.EVERY_DAY.type) {
            builder.append(" * * ?");
        } else if (this.type.type == Type.WORK_DAY.type) {
            builder.append(" ? * MON-FRI");
        } else {
            throw new RuntimeException("cron 格式错误!");
        }

        return builder.toString();
    }


    @Getter
    public enum Type {

        ONCE(0),
        EVERY_DAY(1),
        WORK_DAY(2),
        ;


        Type(int t) {
            this.type = t;
        }

        private int type;

        public static Type getInstance(int t) {
            switch (t) {
                case 0 : return ONCE;
                case 1 : return EVERY_DAY;
                case 2 : return WORK_DAY;
                default:
                    throw new RuntimeException("Error Type!");
            }
        }


    }


}
