package repositories.auctions;

import controllers.auctions.Auction;
import controllers.auctions.CreateAuctionRequest;
import controllers.auctions.UpdateAuctionRequest;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Default
    @ApplicationScoped
public class EntityManagerAuctionRepository implements AuctionRepository {

    @Inject
    EntityManager entityManager;

    private static Auction mapEntityToAuction (AuctionEntity auction) {
        return new Auction(
            auction.getId(),
            auction.getItem_name(),
            auction.getDescription(),
            auction.getAuction_start(),
            auction.getAuction_end(),
            auction.getStarting_bid(),
            auction.getImage_path()
        );
    }

    private static AuctionEntity mapCreateRequestToEntity (CreateAuctionRequest request) {
        return new AuctionEntity(
            request.item_name,
            request.description,
            request.auction_start,
            request.auction_end,
            request.starting_bid,
            request.image_path
        );
    }

    private static AuctionEntity mapUpdateRequestToEntity (UpdateAuctionRequest request) {
        return new AuctionEntity(
            request.item_name,
            request.description,
            request.auction_start,
            request.auction_end,
            request.starting_bid,
            request.image_path
        );
    }

    public List<Auction> findAll() {
        return entityManager.createNamedQuery("AuctionEntity.findAll", AuctionEntity.class)
            .getResultList().stream().map(EntityManagerAuctionRepository::mapEntityToAuction).collect(Collectors.toList());          
    }

    public Auction getById(int id) {
        AuctionEntity auction = entityManager.find(AuctionEntity.class, id);
        return mapEntityToAuction(auction);
    }

    @Transactional
    public Auction create(CreateAuctionRequest request) {
        AuctionEntity newAuction = mapCreateRequestToEntity(request);
        entityManager.persist(newAuction);
        return mapEntityToAuction(newAuction);
    }

    @Transactional
    public Auction delete(int auction_id) {
        AuctionEntity auction = entityManager.find(AuctionEntity.class, auction_id);
        entityManager.remove(auction);
        return mapEntityToAuction(auction);
    }

    @Transactional
    public Auction update(UpdateAuctionRequest request) {
        AuctionEntity updateAuction = mapUpdateRequestToEntity(request);
        entityManager.merge(updateAuction);
        return mapEntityToAuction(updateAuction);
    }
    
}
