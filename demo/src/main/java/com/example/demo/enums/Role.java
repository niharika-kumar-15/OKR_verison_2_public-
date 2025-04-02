package com.example.demo.enums;

public enum Role {
    BASE_EMPLOYEE("BaseEmployee"),
    PROGRAM_DIRECTOR("ProgramDirector");

    private final String value;

    Role(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            System.out.println(r);
            if (r.getValue().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unexpected role value: " + role); // i'm getting this error again and again
    }
}
