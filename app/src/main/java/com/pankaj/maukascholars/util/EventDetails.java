package com.pankaj.maukascholars.util;

import java.io.Serializable;
import java.util.IllegalFormatCodePointException;

/**
 * Project Name: 	<Visual Perception For The Visually Impaired>
 * Author List: 		Pankaj Baranwal
 * Filename: 		<>
 * Functions: 		<>
 * Global Variables:	<>
 */
public class EventDetails implements Serializable{
    private int id = 0;
    private int starred = 0;
    private int saved = 0;
    private String title = "IIDA Student Design Competition";
    private String description = "The Student Design Competition celebrates original design and rewards individuals and/or teams whose projects demonstrate innovative, functional design solutions that have a positive environmental and human impact, while allowing emerging professionals the opportunity to showcase their work and fresh design ideas to professionals working in the field.";
    private String name = "Curated by Spot-on Opportunities";
    private String deadline = "Deadline: 5 February, 2018";
    private String image;
    private String icon;
    private String link = "http://www.iida.org/content.cfm/student-design-competition";
    private String eligibility;
    private String benefits;
    private String requirements;
    private String tags;

    public EventDetails(int id, String title, String description, String deadline, String name, String image, String icon, String link, String eligibility, String requirements, String benefits, String tags){
        this.starred = starred;
        this.saved = saved;
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.name = name;
        this.deadline = deadline;
        this.image = image;
        this.icon = icon;
        this.link = link;
        this.eligibility = eligibility;
        this.benefits = benefits;
        this.requirements = requirements;
        this.tags = tags;

    }

    public EventDetails(int id, int starred, int saved, String title, String description, String deadline, String name, String image, String icon, String link){
        this.starred = starred;
        this.saved = saved;
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.name = name;
        this.deadline = deadline;
        this.image = image;
        this.icon = icon;
        this.link = link;
    }

//    0, 1, 2, 7, 12, 8, 13, 9
    public EventDetails(int id, String title, String description, String deadline, String name, String image, String icon, String link){
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.name = name;
        this.deadline = deadline;
        this.image = image;
        this.icon = icon;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        if (name.length() == 0)
            name = "Curated by Precisely";
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getSaved() {
        return saved;
    }

    public int getStarred() {
        return starred;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getBenifits() {
        return benefits;
    }

    public void setBenifits(String benefits) {
        this.benefits = benefits;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
