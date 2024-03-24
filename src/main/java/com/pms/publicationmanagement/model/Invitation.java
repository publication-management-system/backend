package com.pms.publicationmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String link;

    private Boolean wasTaken;

    public Invitation(Integer id, String link, Boolean wasTaken) {
        this.id = id;
        this.link = link;
        this.wasTaken = wasTaken;
    }

    public Invitation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getWasTaken() {
        return wasTaken;
    }

    public void setWasTaken(Boolean wasTaken) {
        this.wasTaken = wasTaken;
    }
}
