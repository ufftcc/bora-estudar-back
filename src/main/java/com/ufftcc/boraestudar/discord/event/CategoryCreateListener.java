package com.ufftcc.boraestudar.discord.event;

import com.ufftcc.boraestudar.discord.entity.CategoryListener;
import discord4j.core.event.domain.channel.CategoryCreateEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

@Service
public class CategoryCreateListener extends CategoryListener implements EventListener<CategoryCreateEvent> {

    String roleId = "1242374238728491010";

    @Override
    public Class<CategoryCreateEvent> getEventType() {
        return CategoryCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(CategoryCreateEvent event) {
        out.println("Categoria " + event.getCategory().getName() + " criada com sucesso . . .");
        return processCommand(event.getCategory());

    }
}
