package com.pamungkasaji.beshop.model.request.shipping;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class RajaOngkir {

    public Query query;
    public Status status;
    public OriginDetails origin_details;
    public DestinationDetails destination_details;
    public List<Result> results;

    @Data
    public static class Query {
        public String origin;
        public String destination;
        public Integer weight;
        public String courier;
    }

    @Data
    public static class Status {
        public Integer code;
        public String description;
    }

    @Data
    public static class Result {
        public String code;
        public String name;
        public List<Cost> costs;

        @Data
        public static class Cost {
            public String service;
            public String description;
            public List<Cost_> cost = null;

            @NoArgsConstructor
            @Data
            public static class Cost_ {
                public Integer value;
                public String etd;
                public String note;
            }
        }
    }

    @Data
    public static class OriginDetails {
        public String city_id;
        public String province_id;
        public String province;
        public String type;
        public String city_name;
        public String postal_code;
    }

    @Data
    public static class DestinationDetails {
        public String city_id;
        public String province_id;
        public String province;
        public String type;
        public String city_name;
        public String postal_code;
    }
}

