package com.pms.publicationmanagement.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.publicationmanagement.dto.graph.AuthorGraph;
import com.pms.publicationmanagement.dto.graph.LinkDto;
import com.pms.publicationmanagement.dto.graph.NodeDto;
import com.pms.publicationmanagement.model.scraping.ScrapedEntity;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorCitationsPayload;
import com.pms.publicationmanagement.model.scraping.payloads.DocumentPayload;
import com.pms.publicationmanagement.model.scraping.payloads.AuthorProfilePayload;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AuthorGraphMapper {

    public static AuthorGraph toAuthorGraph(List<ScrapedEntity> entityList) {
        List<NodeDto> nodeDtoList = new ArrayList<>();
        List<LinkDto> linkDtoList = new ArrayList<>();

        for (ScrapedEntity entity : entityList) {
            NodeDto nodeDto = mapNode(entity);
            nodeDtoList.add(nodeDto);;


            LinkDto linkDto = mapLink(entity);
            if (linkDto != null) {
                linkDtoList.add(linkDto);
            }
        }

        return new AuthorGraph(nodeDtoList, linkDtoList);
    }


    private static NodeDto mapNode(ScrapedEntity entity) {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(entity.getId().toString());
        nodeDto.setName(mapName(entity));
        nodeDto.setType(entity.getType().name());
        return nodeDto;
    }


    private static LinkDto mapLink(ScrapedEntity entity) {
        if (entity.getParentId() == null) {
            return null;
        }

        LinkDto linkDto = new LinkDto();
        linkDto.setId(UUID.randomUUID().toString());
        linkDto.setTarget(entity.getParentId().toString());
        linkDto.setSource(entity.getId().toString());
        return linkDto;
    }

    private static String mapName(ScrapedEntity entity) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            switch (entity.getType()) {
                case AUTHOR -> {
                    AuthorProfilePayload payload = mapper.readValue(entity.getPayload(), AuthorProfilePayload.class);
                    return payload.getAuthorName();
                }
                case DOCUMENT -> {
                    DocumentPayload payload = mapper.readValue(entity.getPayload(), DocumentPayload.class);
                    return payload.getTitle();
                }
                case CITATION -> {
                    AuthorCitationsPayload payload = mapper.readValue(entity.getPayload(), AuthorCitationsPayload.class);
                    return payload.getTitle();
                }
                default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("Error while parsing entity", e);
            return null;
        }
    }
}
