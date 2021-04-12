package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.kinghp.portfolio_manager.models.Paper;
import ru.kinghp.portfolio_manager.models.Portfolio;
import ru.kinghp.portfolio_manager.models.PortfoliosPaper;
import ru.kinghp.portfolio_manager.repository.PaperRepository;
import ru.kinghp.portfolio_manager.service.PaperService;
import ru.kinghp.portfolio_manager.service.PortfolioService;
import ru.kinghp.portfolio_manager.service.PortfoliosPaperService;
import ru.kinghp.portfolio_manager.ws.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Endpoint
public class PortfolioEndpoint {

    private static final String NAMESPACE_URI = "http://www.ws.portfolio_manager.kinghp.ru";

    private final PaperService paperService;
    private final PortfolioService portfolioService;
    private final PortfoliosPaperService portfoliosPaperService;
    private final PaperRepository paperRepository;
    private final ModelMapper modelMapper;


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllPapersRequest")
    @ResponsePayload
    public GetAllPapersResponse getAllPapers(){
        GetAllPapersResponse response = new GetAllPapersResponse();
        List<Paper> papers = (List) paperRepository.findAll();
        List<PaperDto> paperDtos = papers.stream().map(this::convertPaperToDto).collect(Collectors.toList());
        response.getPaperDto().addAll(paperDtos);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPaperDetailsRequest")
    @ResponsePayload
    public GetPaperDetailsResponse getPaper(@RequestPayload GetPaperDetailsRequest request){
        GetPaperDetailsResponse response = new GetPaperDetailsResponse();
        Paper paper = paperRepository.findById(request.getId()).get();
        response.setPaperDto(convertPaperToDto(paper));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postAddPaperRequest")
    @ResponsePayload
    public PostAddPaperResponse postAddPaper(@RequestPayload PostAddPaperRequest request){
        PostAddPaperResponse response = new PostAddPaperResponse();
        Paper paper = paperService.add(convertDtoToPaper(request.getPaperDto()));
        response.setPaperDto(convertPaperToDto(paper));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postPaperUpdateRequest")
    @ResponsePayload
    public PostPaperUpdateResponse postPaperUpdate(@RequestPayload PostPaperUpdateRequest request){
        PostPaperUpdateResponse response = new PostPaperUpdateResponse();
        Paper paper = paperService.update(convertDtoToPaper(request.getPaperDto()));
        response.setPaperDto(convertPaperToDto(paper));
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllPortfoliosRequest")
    @ResponsePayload
    public GetAllPortfoliosResponse getAllPortfolios(){
        GetAllPortfoliosResponse response = new GetAllPortfoliosResponse();
        List<Portfolio> portfolios = (List) portfolioService.findAll();
        List<PortfolioDto> portfolioDtos = portfolios.stream().map(this::convertPortfolioToDto).collect(Collectors.toList());
        response.getPortfolioDto().addAll(portfolioDtos);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postAddPortfolioRequest")
    @ResponsePayload
    public PostAddPortfolioResponse postAddPortfolio(@RequestPayload PostAddPortfolioRequest request){
        PostAddPortfolioResponse response = new PostAddPortfolioResponse();
        Portfolio portfolio = portfolioService.add(convertDtoToPortfolio(request.getPortfolioDto()));
        PortfolioDto portfolioDto = convertPortfolioToDto(portfolio);
        portfolioDto.getPapersDto().
                addAll(portfolio.getPapers().stream().map(this::convertPortfoliosPaperToDto).collect(Collectors.toList()));
        response.setPortfolioDto(portfolioDto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPortfolioDetailsRequest")
    @ResponsePayload
    public GetPortfolioDetailsResponse getPortfolio(@RequestPayload GetPortfolioDetailsRequest request){
        GetPortfolioDetailsResponse response = new GetPortfolioDetailsResponse();
        Portfolio portfolio = portfolioService.findById(request.getId()).get();
        PortfolioDto portfolioDto = convertPortfolioToDto(portfolio);
        portfolioDto.getPapersDto().
                addAll(portfolio.getPapers().stream().map(this::convertPortfoliosPaperToDto).collect(Collectors.toList()));
        response.setPortfolioDto(portfolioDto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postPortfolioAddPaperRequest")
    @ResponsePayload
    public PostPortfolioAddPaperResponse postPortfolioAddPaper(@RequestPayload PostPortfolioAddPaperRequest request){
        if (!paperService.existsById(request.getAddingPaperId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paper not found");
        }
        if (!portfolioService.existsById(request.getPortfolioId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }

        PostPortfolioAddPaperResponse response = new PostPortfolioAddPaperResponse();
        Portfolio portfolio = portfolioService.addPaper(request.getPortfolioId(),
                request.getAddingPaperId(),
                new PortfoliosPaper(),
                request.getVol());

        PortfolioDto portfolioDto = convertPortfolioToDto(portfolio);
        portfolioDto.getPapersDto().
                addAll(portfolio.getPapers().stream().map(this::convertPortfoliosPaperToDto).collect(Collectors.toList()));
        response.setPortfolioDto(portfolioDto);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postPortfolioRemovePaperRequest")
    @ResponsePayload
    public PostPortfolioRemovePaperResponse postPortfolioRemovePaper(@RequestPayload PostPortfolioRemovePaperRequest request){
        if (!portfoliosPaperService.existsById(request.getRemovingPaperId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Paper not found");
        }
        if (!portfolioService.existsById(request.getPortfolioId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        }

        PostPortfolioRemovePaperResponse response = new PostPortfolioRemovePaperResponse();
        Portfolio portfolio = portfolioService.removePaper(request.getPortfolioId(), request.getRemovingPaperId());

        PortfolioDto portfolioDto = convertPortfolioToDto(portfolio);
        portfolioDto.getPapersDto().
                addAll(portfolio.getPapers().stream().map(this::convertPortfoliosPaperToDto).collect(Collectors.toList()));
        response.setPortfolioDto(portfolioDto);
        return response;
    }


    //todo переделать
    private PaperDto convertPaperToDto(Paper paper){
        return modelMapper.map(paper, PaperDto.class);
    }
    private Paper convertDtoToPaper(PaperDto paperDto){
        return modelMapper.map(paperDto, Paper.class);
    }

    private PortfolioDto convertPortfolioToDto(Portfolio portfolio){ return modelMapper.map(portfolio, PortfolioDto.class); }
    private Portfolio convertDtoToPortfolio(PortfolioDto portfolioDto){ return modelMapper.map(portfolioDto, Portfolio.class); }

    private PortfoliosPaperDto convertPortfoliosPaperToDto(PortfoliosPaper portfoliosPaper){ return modelMapper.map(portfoliosPaper, PortfoliosPaperDto.class); }
    private PortfoliosPaper convertDtoToPortfoliosPaper(PortfoliosPaperDto portfoliosPaperDto){ return modelMapper.map(portfoliosPaperDto, PortfoliosPaper.class); }
}
